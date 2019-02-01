# -*- coding: utf-8 -*-

# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: https://doc.scrapy.org/en/latest/topics/item-pipeline.html
from pymongo import MongoClient

class ZhongwenwangPipeline(object):
    def process_item(self, item, spider):
#         client = MongoClient('127.0.0.1',27017)
#         collection = client["scrapy"]["mm123"]
#         collection.insert(item)
        print('-----------------')
        return item
