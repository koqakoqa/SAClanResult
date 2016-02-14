package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetResult{
	public static int page = 1;
	public static List<String> urls;
	public static Map map = new Map();
	public static String mode = "clan";
	public static void main(String[] argv) throws Exception {

		setting();

		Chrome.Setup();
		Chrome.run();

		urls = new ArrayList<>();
		GetResult.getUrl();

		try{
			File file = new File("log/urls.txt");
			FileWriter filewriter = new FileWriter(file);

			urls.forEach(url -> {
				try {
					filewriter.append(url);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});

			filewriter.close();
		}catch(IOException e){
			System.out.println(e);
		}
		urls.forEach(url -> {
			getKD(url);
		});
		map.newExcel();
	}
	public static void getUrl() throws Exception {
		boolean flg = false;
		try{
			File file = new File("log/html" + page + ".txt");
			BufferedReader br = new BufferedReader(new FileReader(file));

			String str;
			while((str = br.readLine()) != null){
				if (str.indexOf("<span>クラン戦記録</span>") != -1) {
					flg = true;
				} else if (str.indexOf("<div class=\"paging\">") != -1) {
					flg = false;
				}
				if (flg) {
					if (str.indexOf("<td><a href=\"clanrecord_result.aspx") != -1) {
						urls.add("http://sa.nexon.co.jp/clan/clanrecord/clanrecord_result.aspx" + str.substring(51, 86) + "\n");
					}
				}
			}

			br.close();
		}catch(FileNotFoundException e){
			return;
		}catch(IOException e){
			System.out.println(e);
		}
		page++;
		getUrl();
	}

	public static void getKD(String _url) {
		try {
			URL url = new URL(_url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			String charset = "charset=utf-8";
			String encoding = Arrays.asList(charset.split("=") ).get(1);
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), encoding ));
			String line;
			boolean flg = false;
			int cnt = 0;
			while ((line = in.readLine()) != null) {
				if (line.indexOf("<h1>Conflit</h1>") != -1) {
					flg = true;
				} else if (line.indexOf("</div>") != -1 && flg) {
					flg = false;
				}
				if (flg) {
					if (cnt > 10) {
						if (line.indexOf("alt=\"階級\">") != -1) {
							map.insert(cut(line), cut(in.readLine()), cut(in.readLine()));
						}
					}
					cnt++;
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String cut(String line) {
		String ret = "";
		boolean flg = false;
		for (int i = 0; i < line.length(); i++) {
			String temp = line.substring(i, i + 1);
			if (temp.equals("<")) {
				flg = true;
			} else if (temp.equals(">")) {
				flg = false;
			} else if (flg == false) {
				ret = ret.concat(temp);
			}
		}
		String ret2 = ret.replaceAll("\t", "");
		return ret2;
	}

	public static void setting() {
		try{
			File file = new File("setting.conf");
			BufferedReader br = new BufferedReader(new FileReader(file));

			String str;
			while((str = br.readLine()) != null){
				String[] setting = str.split(":", 0);
				System.out.println(setting[0] + ":" + setting[1]);
				if (setting[0].equals("mode")) {
					mode = setting[1];
				}
			}

			br.close();
		}catch(FileNotFoundException e){
			return;
		}catch(IOException e){
			System.out.println(e);
		}
	}
}