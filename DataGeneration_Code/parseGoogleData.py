import json
from pprint import pprint
import pandas as pd
import populartimes
import glob as gb
import os
import requests
import pickle

googledetailsURI = 'https://maps.googleapis.com/maps/api/place/details/json?placeid={}&key={}'
# googleMapsAPI_Key = 'AIzaSyBmJWU0UmgjW7L5opvdhC4nrPVAgqgcnMU' #vshws.v7@gmail.com
# googleMapsAPI_Key ='AIzaSyBlUw41ARsJcR7fpTE8Lz8zFaT8Ky0SAn8' #vishwas.venkatachalapathy1989@gmail.com
# googleMapsAPI_Key = 'AIzaSyC1b1aZLMuxSXwmBoAxct9XxsWIjaTmEMM' # vv3156@g.rit.edu
googleMapsAPI_Key = '{}'#ashwini

lat = []
long = []
resturant_name = []
id = []
google_ID = []
rating = []
food_type = []
good_ratings =[]
bad_ratings=[]
price_rate =[]
add=[]
def getreviews(id):
    good = 0
    bad = 0
    rp = requests.get(googledetailsURI.format(id, googleMapsAPI_Key))
    r=rp.json()
    try:
        p = r['result']['price_level']
    except KeyError:
        p = 0
    try:
        rtemp = r['result']['reviews']
    except KeyError:
        return good, bad, p

    if (len(rtemp) > 0):
        for re in rtemp:
            if (re['rating'] > 2.5):
                good = good + 1
            else:
                bad = bad + 1
    return good, bad, p

count=0
for filename in gb.glob('{}/data/*.json'.format(os.getcwd())):
    decode_data = json.load(open(filename, encoding="utf8"))
    fd_type = os.path.basename(filename)[:-5]
    for data in decode_data['results']:
        if data['place_id'] not in google_ID:
            resturant_name.append(data['name'])
            lat.append(data['geometry']['location']['lat'])
            long.append(data['geometry']['location']['lng'])
            id.append(data['id'])
            google_ID.append(data['place_id'])
            add.append(data['formatted_address'])
            g,b,price = getreviews(data['place_id'])
            good_ratings.append(g)
            bad_ratings.append(b)
            price_rate.append(price)
            count=count+1
            try:
                rating.append(data['rating'])
            except KeyError:
                rating.append(0)
            food_type.append(fd_type)



print(count)


raw_data = {'Name': resturant_name,
        'Latitude': lat,
        'Longitude': long,
        'ID': id,
        'Google Place ID': google_ID,
        'Rating':rating,
        'Food_type':food_type,
        'Formated Address':add,
        'Good reviews':good_ratings,
        'Bad reviews':bad_ratings,
        'Price rating': price_rate,

            }

with open('google_data_dump.pkl', 'wb') as f:  # Python 3: open(..., 'wb')
    pickle.dump([raw_data], f)




with open('google_data_dump.pkl','rb') as f:  # Python 3: open(..., 'rb')
    rt = pickle.load(f)
r=rt[0]
raw_data = {'Name': r['Name'],
        'Latitude': r['Latitude'],
        'Longitude': r['Longitude'],
        'ID':r['ID'],
        'Google Place ID':r['Google Place ID'],
        'Rating':r['Rating'],
        'Food_type':r['Food_type'],
        'Formated Address':r['Formated Address'],
        'Good reviews':r['Good reviews'],
        'Bad reviews':r['Bad reviews'],
        'Price rating': r['Price rating'],

            }
print('{}\\data\\Resturant_Data.csv'.format(os.getcwd()))
df = pd.DataFrame(raw_data, columns =['Name','Latitude','Longitude','ID','Google Place ID','Rating','Food_type','Formated Address','Good reviews','Bad reviews','Price rating'])

df.to_csv('{}\\data\\Resturant_Data.csv'.format(os.getcwd()))
