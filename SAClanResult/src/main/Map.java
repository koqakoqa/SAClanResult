package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Map {

	List<String> nameList;
	List<List<Integer>> killList;
	List<List<Integer>> deathlist;

	Map () {
		nameList = new ArrayList<>();
		killList = new ArrayList<>();
		deathlist = new ArrayList<>();
	}

	public void insert(String name, String kill, String death) {
		if (!nameList.contains(name)) {
			nameList.add(name);
			killList.add(new ArrayList<>());
			deathlist.add(new ArrayList<>());
		}
		int num = nameList.indexOf(name);
		killList.get(num).add(Integer.parseInt(kill));
		deathlist.get(num).add(Integer.parseInt(death));
	}

	public void view() {
		nameList.forEach(name -> {
			int num = nameList.indexOf(name);
			System.out.println("Name : " + name);
			List<Integer> kl = killList.get(num);
			List<Integer> dl = deathlist.get(num);
			for (int i = 0; i < kl.size(); i++) {
				System.out.println(kl.get(i) + " : " + dl.get(i));
			}
		});
	}

	public void viewExcel() {
		int i = 0;
		int j = 0;
		int max = 0;
		for (int k = 0; k < killList.size(); k++) {
			List<Integer> list = killList.get(k);
			if (list.size() > max) {
				max = list.size();
			}
		};
		nameList.forEach(name -> {
			System.out.print(name + "\t\t");
		});
		System.out.println();
		for (i = 0; i < max; i++) {
			for (j = 0; j < killList.size(); j++) {
				try {
					System.out.print(killList.get(j).get(i) + "\t" + deathlist.get(j).get(i) + "\t");
				} catch (Exception e) {
					System.out.print("\t\t");
				}
			}
			System.out.println();
		}
	}

	public void newExcel() {
		int i = 0;
		int j = 0;
		int max = 0;
		for (int k = 0; k < killList.size(); k++) {
			List<Integer> list = killList.get(k);
			if (list.size() > max) {
				max = list.size();
			}
		};
		try{
			File file = new File("result.txt");
			FileWriter filewriter = new FileWriter(file);

			for (int nameCnt = 0 ; nameCnt < nameList.size(); nameCnt++) {
				filewriter.append(nameList.get(nameCnt) + "\t\t");
			};

			filewriter.append("\n");
			for (i = 0; i < max; i++) {
				for (j = 0; j < killList.size(); j++) {
					try {
						filewriter.append(killList.get(j).get(i) + "\t" + deathlist.get(j).get(i) + "\t");
					} catch (Exception e) {
						filewriter.append("\t\t");
					}
				}
				filewriter.append("\n");
			}

			filewriter.close();
		}catch(IOException e){
			System.out.println(e);
		}
	}
}
