package practice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.select.Elements;

public class FileImage {
	public static void main(String[] args) throws Exception {
		//imageURL();
		String a ="com/images/banner1.jpg";
		int i = a.lastIndexOf("/");
		System.out.println(i);
	}

	public static void doImages(String imgURL, String filePath)
			throws Exception {
		String fileName = imgURL.substring(imgURL.lastIndexOf("/"));
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		URL url = new URL(imgURL);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		InputStream is = connection.getInputStream();
		File file2 = new File(filePath + fileName);
		FileOutputStream out = new FileOutputStream(file2);
		int i = 0;
		while ((i = is.read()) != -1) {
			out.write(i);
		}
		is.close();
		out.close();
	}

	public static String imageURL() throws Exception {
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet("http://www.csmzxy.com/");
		org.apache.http.HttpHost proxy = new org.apache.http.HttpHost(
				"221.4.133.67", 8080, "http");
		RequestConfig config = RequestConfig.custom().setSocketTimeout(2000)
				.setConnectTimeout(1000 * 10).setProxy(proxy).build();
		httpGet.setConfig(config);
		HttpResponse response = client.execute(httpGet);
		String string = EntityUtils.toString(response.getEntity(), "UTF-8");
		Document document = Jsoup.parse(string);
		Element elementById = document.getElementById("content");
		Elements tag = document.getElementsByTag("img");
		Elements select = document.select("img[src$=.png]");
		System.out.println(select);
		Element first = document.select("div.masthead").first();
		int i = 1;
		for (Element link : tag) {
			String attr = link.attr("src");
			String text = link.text();
			String filePath = "C:\\Users\\Administrator\\Desktop\\picture\\";
			if (attr.contains("http")) {
				doImages(attr, filePath);
				i += 1;
			} else {
				doImages("http://www.csmzxy.com/" + attr, filePath);
				i += 1;
			}
			System.out.println("成功打印" + i + "张图片");
		}
		return null;
	}
}
