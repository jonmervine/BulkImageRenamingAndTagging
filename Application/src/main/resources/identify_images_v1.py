#!/usr/bin/env python -u
#This script requires Python 3+, Requests, and Pillow, a modern fork of PIL, the Python Imaging Library: 'easy_install Pillow' and 'easy_install requests'
#For Windows easy_install setup, download and run: https://bitbucket.org/pypa/setuptools/raw/bootstrap/ez_setup.py
#After Installation of easy_install, it will be located in the python scripts directory.

#This is a basic, likley broken example of how to use the very beta saucenao API...
#There are several signifigant holes in the api, and in the way in which the site responds and reports error conditions.
#These holes will likley be filled at some point in the future, and it may impact the status checks used below.

#THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. 
#################CONFIG##################

api_key="!!!REPLACE THIS WITH YOUR API KEY!!!"
EnableRename=False
minsim='80!'#forcing minsim to 80 is generally safe for complex images, but may miss some edge cases. If images being checked are primarily low detail, such as simple sketches on white paper, increase this to cut down on false positives.

##############END CONFIG#################
import sys
import os
import io
import unicodedata
import requests
from PIL import Image
import json
import codecs
import re
import time
from collections import OrderedDict
sys.stdout = codecs.getwriter('utf8')(sys.stdout.detach())
sys.stderr = codecs.getwriter('utf8')(sys.stderr.detach())

extensions = {".jpg", ".jpeg", ".png", ".gif", ".bmp"}
thumbSize = (150,150)

#enable or disable indexes
index_hmags='0'
index_hanime='0'
index_hcg='0'
index_ddbobjects='0'
index_ddbsamples='0'
index_pixiv='1'
index_pixivhistorical='1'
index_anime='0'
index_seigaillust='1'
index_danbooru='0'
index_drawr='1'
index_nijie='1'
index_yandere='0'

#generate appropriate bitmask
db_bitmask = int(index_yandere+index_nijie+index_drawr+index_danbooru+index_seigaillust+index_anime+index_pixivhistorical+index_pixiv+index_ddbsamples+index_ddbobjects+index_hcg+index_hanime+index_hmags,2)

#encoded print - handle random crap
def printe(line):
    print(str(line).encode(sys.getdefaultencoding(), 'replace')) #ignore or replace

for root, _, files in os.walk(u'../../../../src/resources', topdown=False):
	for f in files:
		fname = os.path.join(root, f)
		for ext in extensions:
			if fname.lower().endswith(ext):
				print(fname)
				image = Image.open(fname)
				image.thumbnail(thumbSize, Image.ANTIALIAS)
				imageData = io.BytesIO()
				image.save(imageData,format='PNG')
				
				url = 'http://saucenao.com/search.php?output_type=2&numres=1&minsim='+minsim+'&dbmask='+str(db_bitmask)+'&api_key='+api_key
				files = {'file': ("image.png", imageData.getvalue())}
				imageData.close()
				
				processResults = True
				while True:
					r = requests.post(url, files=files)
					if r.status_code != 200:
						if r.status_code == 403:
							print('Incorrect or Invalid API Key! Please Edit Script to Configure...')
							sys.exit(1)
						else:
							#generally non 200 statuses are due to either overloaded servers or the user is out of searches
							print("status code: "+str(r.status_code))
							time.sleep(10)
					else:
						results = json.JSONDecoder(object_pairs_hook=OrderedDict).decode(r.text)
						if int(results['header']['user_id'])>0:
							#api responded
							print('Remaining Searches 30s|24h: '+str(results['header']['short_remaining'])+'|'+str(results['header']['long_remaining']))
							if int(results['header']['status'])==0:
								#search succeeded for all indexes, results usable
								break
							else:
								if int(results['header']['status'])>0:
									#One or more indexes are having an issue.
									#This search is considered partially successful, even if all indexes failed, so is still counted against your limit.
									#The error may be transient, but because we don't want to waste searches, allow time for recovery.
									print('API Error. Retrying in 600 seconds...')
									time.sleep(600)
								else:
									#Problem with search as submitted, bad image, or impossible request.
									#Issue is unclear, so don't flood requests.
									print('Bad image or other request error. Skipping in 10 seconds...')
									processResults = False
									time.sleep(10)
									break
						else:
							#General issue, api did not respond. Normal site took over for this error state.
							#Issue is unclear, so don't flood requests.
							print('Bad image, or API failure. Skipping in 10 seconds...')
							processResults = False
							time.sleep(10)
							break
				
				if processResults:
					#print(results)
					
					if int(results['header']['results_returned']) > 0:
						#one or more results were returned
						if float(results['results'][0]['header']['similarity']) > float(results['header']['minimum_similarity']):
							print('hit! '+str(results['results'][0]['header']['similarity']))

							#get vars to use
							service_name = ''
							illust_id = 0
							member_id = results['results'][0]['data']['member_id']
							index_id = results['results'][0]['header']['index_id']
							page_string = ''
							page_match = re.search('(_p[\d]+)\.', results['results'][0]['header']['thumbnail'])
							if page_match:
								page_string = page_match.group(1)
								
							if index_id == 5 or index_id == 6:
								#5->pixiv 6->pixiv historical
								service_name='pixiv'
								illust_id=results['results'][0]['data']['pixiv_id']
							elif index_id == 8:	
								#8->nico nico seiga
								service_name='seiga'
								illust_id=results['results'][0]['data']['seiga_id']
							elif index_id == 10:
								#10->drawr
								service_name='drawr'
								illust_id=results['results'][0]['data']['drawr_id']								
							elif index_id == 11:
								#11->nijie
								service_name='nijie'
								illust_id=results['results'][0]['data']['nijie_id']
							else:
								#unknown
								print('Unhandled Index! Exiting...')
								sys.exit(2)
								
							try:
								newfname = os.path.join(root, service_name+'_'+str(member_id)+'_'+str(illust_id)+page_string+'.'+fname.split(".")[-1].lower())
								print('New Name: '+newfname)
								if EnableRename:
									os.rename(fname, newfname)
							except Exception as e:
								print(e)
								sys.exit(3)
							
						else:
							print('miss... '+str(results['results'][0]['header']['similarity']))
							
					else:
						print('no results... ;_;')

					if int(results['header']['long_remaining'])<1: #could potentially be negative
						print('Out of searches for today. Sleeping for 6 hours...')
						time.sleep(6*60*60)
					if int(results['header']['short_remaining'])<1:
						print('Out of searches for this 30 second period. Sleeping for 25 seconds...')
						time.sleep(25)
						
print('All Done!')