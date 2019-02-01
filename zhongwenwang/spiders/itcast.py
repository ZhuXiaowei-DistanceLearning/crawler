# -*- coding: utf-8 -*-
import scrapy
import time
from twisted.spread.pb import respond
import os


class ItcastSpider(scrapy.Spider):
    name = 'itcast'
    start_urls = ['http://www.mm131.com/xinggan/']
    page = 1;
    base_dir = "C:\\Users\\zxw\\Desktop\\qiubai/"

    def parse(self, response):
        # 处理start_url 地址对应的相应
        li_list = response.xpath("//dl[contains(@class,'list-left')]//dd")
        for li in li_list:
            item = {}
            if li.xpath("./a/text()").extract_first() == "2":
                continue
            else:
                item["name"] = self.base_dir+li.xpath("./a/text()").extract_first()
                item["url"] = li.xpath("./a/@href").extract_first()
#             item["name"] = name
#             item["url"] = url
                try:
                    os.makedirs(self.base_dir)
                    os.chdir(self.base_dir)
                except:
                    os.chdir(self.base_dir)
                    pass
                os.mkdir(item["name"])
                yield scrapy.Request(item["url"], callback=self.parse_img_list, meta={"item":item})
            
    def parse_img_list(self, response):
            item = response.meta["item"]
            os.chdir(item["name"])
            imgurl = response.xpath("//div[@class='content-pic']/a/img/@src").extract_first()
            filename = response.xpath("//div[@class='content-pic']/a/img/@alt").extract_first()
            next_url = response.xpath("//div[@class='content-pic']/a/@href").extract_first()
            item["filename"] = filename
            item["next_url"] = "http://www.mm131.com/xinggan/" + next_url
            yield scrapy.Request(imgurl, callback=self.dowmload_img, meta={"item":item})
        
    def dowmload_img(self, response):
        item = response.meta["item"]
        img = response.body
        with open(item["filename"]+".png","wb") as f:
             f.write(img)
    
        yield scrapy.Request(item["next_url"], callback=self.parse_img_list, meta={"item":item})
    
    def next_url(self, response):
        item = {}
        imgurl = response.xpath("//div[@class='content-pic']/a/img/@src").extract_first()
        filename = response.xpath("//div[@class='content-pic']/a/img/@alt").extract_first()
        try:
            next_url = response.xpath("//div[@class='content-page']//a[@class='page-ch'][2]/@href").extract_first()
        except:
            next_url = ""
        if next_url == "":
            pass
        else:
            item["filename"] = filename
           # item["next_url"] = "http://www.mm131.com/xinggan/" + next_url
            yield scrapy.Request(imgurl, callback=self.parse_img_list, meta={"item":item})
        
        
