import java.io.*;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonMergeFiles
{
	
	public static void main(String[] args) throws FileNotFoundException
	{
		File f=null;
		String[] names;
		
		TreeSet<Integer> list=new TreeSet<Integer>();
		try
		{
			f=new File("inputdata");
			
			names=f.list();
			
			for(String name: names)
			{
				int num=check_file(name);
				if(num!=-1)
					list.add(num);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace(); 
		}
		
		merge(list);
	}
	
	
	
	static int check_file(String name)
	{
		String type[]=name.split("\\.");
				int n=type[0].length();
				if(type[1].equals("json") && type[0].substring(0,n-1).equals("data"))
				{
					String value=type[0].substring(4,n);
					int file_num;
					try
					{
						file_num=Integer.parseInt(value);
						return file_num;
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
				return -1;
	}
	
	
	
	static void merge(TreeSet<Integer> list)
	{
		JSONParser jsonParser = new JSONParser();
		
		JSONObject write_obj = new JSONObject(); 
		JSONArray write_arr = new JSONArray();
		
		Iterator<Integer> value=list.iterator();
		
		while(value.hasNext())
		{
			try (FileReader reader = new FileReader("inputdata/data"+value.next()+".json"))
			{
				Object obj = jsonParser.parse(reader);
				JSONObject read_obj = (JSONObject) obj; 
				JSONArray read_arr = (JSONArray) read_obj.get("student");
				
				Iterator<JSONObject> itr2 =read_arr.iterator(); 
          
				while (itr2.hasNext())  
				{ 
					LinkedHashMap<String,String> m=new LinkedHashMap<String,String>();
					Iterator<Map.Entry> itr1 = ((Map) itr2.next()).entrySet().iterator(); 
					while (itr1.hasNext()) { 
						Map.Entry pair = itr1.next(); 
						m.put(""+pair.getKey(), ""+pair.getValue()); 
					} 
					write_arr.add(m);
				} 
				
			}
			catch(Exception e)
			{
				e.printStackTrace(); 
			}
		}
			
		write_obj.put("students",write_arr);
	
		try
		{
			FileWriter pw = new FileWriter("output/merge.json"); 
			pw.write(write_obj.toJSONString()); 
			pw.flush(); 
            pw.close(); 
		}
		catch(Exception e)
		{
			e.printStackTrace(); 
		}
        
	}
}
