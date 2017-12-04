import requests
import pandas as pd
import numpy as np
import calendar
import os
import random
import populartimes
import populartimes.crawler as cr
import matplotlib.pyplot as plt
import pickle
from math import sin, cos, sqrt, atan2
import geopy.distance
# %matplotlib inline
org_coor = (37.391219,-121.982070)
# googleMapsAPI_Key = 'AIzaSyBmJWU0UmgjW7L5opvdhC4nrPVAgqgcnMU' #vshws.v7@gmail.com
# googleMapsAPI_Key ='AIzaSyBlUw41ARsJcR7fpTE8Lz8zFaT8Ky0SAn8' #vishwas.venkatachalapathy1989@gmail.com
# googleMapsAPI_Key = 'AIzaSyC1b1aZLMuxSXwmBoAxct9XxsWIjaTmEMM' # vv3156@g.rit.edu
googleMapsAPI_Key = {}#ashwini
detail_url = "https://maps.googleapis.com/maps/api/place/details/json?placeid={}&key={}"
x_details = []
df = pd.read_csv('{}\\data\\Resturant_Data.csv'.format(os.getcwd()),encoding='cp1252')
for i in range(0,len(df['Name'])):
    popularity, rating, rating_n = cr.get_populartimes("{} {}".format(df['Name'][i],df['Formated Address'][i]))
    current_popularity = cr.get_current_popularity("{} {}".format(df['Name'][i],df['Formated Address'][i]))

    populartimes_json, days_json = [], [[0 for _ in range(24)] for _ in range(7)]
    # get popularity for each day
    if popularity is not None:
        for day in popularity:

            day_no, pop_times = day[:2]

            if pop_times is not None:
                for el in pop_times:

                    hour, pop = el[:2]
                    days_json[day_no - 1][hour] = pop

                    # day wrap
                    if hour == 23:
                        day_no = day_no % 7 + 1

        # {"name" : "monday", "data": [...]} for each weekday as list
        populartimes_json = [
            {
                "name": list(calendar.day_name)[d],
                "data": days_json[d]
            } for d in range(7)
        ]

    x_details.append(populartimes_json)
    print(i)



# Saving the objects:
with open('populartimes_dump.pkl', 'wb') as f:  # Python 3: open(..., 'wb')
    pickle.dump([x_details], f)
# print(pop_dump)



with open('populartimes_dump.pkl','rb') as f:  # Python 3: open(..., 'rb')
    x_details = pickle.load(f)
days = list(calendar.day_name)

names = df['Name']
capacity = [random.randint(15, 80) for x in range(len(names))]
good_reviews = [random.randint(1, 2500) for x in range(len(names))]
bad_reviews = [random.randint(1, 2500) for x in range(len(names))]
price_ratings = [random.uniform(1, 4.95) for x in range(len(names))]
pop_times = x_details[0]
random_num = [random.randint(0,len(names)-1) for x in range(10000)]
w_all = [random.randint(0, 5) for x in range(len(names))]

weather = ["Thunderstorm","Rain","Drizzle","Snow","Clouds","Clear"]
weather_k = [20,15,5,-5,-10,-25]

food =  ["Chinese","Mexican","Indian","Greek","Mediterranean","Thai","Italian"]
foodtp = [7,5,10,6,4,6,8]

day =[.3,.2,.4,.3,.80,0.90,1.0]


to_return_Name=[]
to_return_Rating=[]
to_return_Distance=[]
to_return_Day=[]
to_return_Occupancy=[]
to_return_Capacity=[]
to_return_Weather=[]
to_return_Food_type=[]
to_return_Good_reviews=[]
to_return_Bad_reviews=[]
to_return_Price_ratings=[]
to_return_AmountofFoodCooked=[]

to_return_Wastage=[]
pop_times_mod = []
for i in range(0,len(pop_times)):
    da =[]
    maxx=0
    for j in range(0,len(pop_times[i])):
        temp = np.sum(np.asarray(pop_times[i][j]['data']))
        if(temp>maxx):
            maxx=temp
        da.append(temp)
    pop_times_mod.append(np.asarray(da)/maxx)

pop_times_mod = np.asarray(pop_times_mod)
for i in range(0,len(random_num)):
    if(len(pop_times[random_num[i]])>0):
        k = random_num[i]
        day_idx=random.randint(0, 6)
        res_name  = names[k]
        res_rating = df['Rating'][k]
        # occupancy = np.sum(np.asarray(pop_times[k][day_idx]['data']))
        occupancy = pop_times_mod[k][day_idx]
        distance = geopy.distance.vincenty(org_coor,(df['Latitude'][k],df['Longitude'][k])).km
        cap = capacity[k]
        fd_type = df['Food_type'][k]
        gd_reviews = good_reviews[k]
        bd_reviews = bad_reviews[k]
        price_rating = price_ratings[k]
        amnt_cooked = random.randint(15, 175)
        wea =weather[w_all[k]]
        temp = (((occupancy * (res_rating/5 )*(bd_reviews/gd_reviews) + foodtp[food.index(fd_type)] )+ weather_k[w_all[k]]) * distance )*(price_rating)/5
        if (amnt_cooked > cap*.80):
            temp = temp + 10
        else:
            temp = temp - 10

        if (round(temp) > cap):
            temp = temp * (1-day[day_idx])
        else:
            temp = temp + (1-day[day_idx]) * cap
        to_return_Name.append(res_name)
        to_return_Rating.append(res_rating)
        to_return_Distance.append(distance)
        to_return_Day.append(days[day_idx])
        to_return_Occupancy.append(occupancy)
        to_return_Capacity.append(cap)
        to_return_Weather.append(wea)
        to_return_Food_type.append(fd_type)
        to_return_Good_reviews.append(gd_reviews)
        to_return_Bad_reviews.append(bd_reviews)
        to_return_Price_ratings.append(price_rating)
        to_return_AmountofFoodCooked.append(amnt_cooked)

        to_return_Wastage.append(temp)

to_return_Wastage = np.asarray(to_return_Wastage)
to_return_Wastage = to_return_Wastage-to_return_Wastage.min()

to_return_Wastage=to_return_Wastage/to_return_Wastage.max()

raw_data = {'Name': to_return_Name,
        'Ratings': to_return_Rating,
        'Distance': to_return_Distance,
        'Day':to_return_Day,
        'Occupency':to_return_Occupancy,
        'Capacity':to_return_Capacity,
        'Weather':to_return_Weather,
        'foodType':to_return_Food_type,
        'goodReviews':to_return_Good_reviews,
        'badReviews':to_return_Bad_reviews,
        'AmountofFoodCookedfor': to_return_AmountofFoodCooked,
        'Price_rating':to_return_Price_ratings,
        'Wastage':to_return_Wastage}

df = pd.DataFrame(raw_data, columns =['Name','Ratings','Distance','Day','Occupency','Capacity','Weather','foodType','goodReviews','badReviews', 'AmountofFoodCookedfor', 'Price_rating','Wastage'])

df.to_csv('{}\\data\\Output_test.csv'.format(os.getcwd()))



plt.hist(to_return_Wastage)
plt.show()
