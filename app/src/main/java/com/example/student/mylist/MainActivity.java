package com.example.student.mylist;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Environment;
import android.support.annotation.ColorRes;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static android.graphics.Color.parseColor;


public class MainActivity extends ActionBarActivity {

    String[] colourNames;
    String myColor;
    String colorV[];
    String sdw;
    int data_block = 100;
    ListView lv;
    View back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        colourNames = getResources().getStringArray(R.array.listArray);
        Resources res =getResources();
        colorV = res.getStringArray(R.array.listValues);
        lv = (ListView) findViewById(R.id.listView);

        ArrayAdapter aa = new ArrayAdapter(this, R.layout.activitiy_listview, colourNames);
        lv.setAdapter(aa);
        back = findViewById(R.id.relative);

        String checksd = Check_sd(); //calls the function to check stored color in sd card

        if(checksd==""){
            back.setBackgroundColor(DEFAULT_KEYS_DIALER);
        }
        else {
            checksd=checksd.substring(2);
            back.setBackgroundColor(Color.parseColor("#"+checksd));
        }


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view, int position, long id) {


                myColor = colorV[position].substring(2);

                back.setBackgroundColor(Color.parseColor("#"+myColor));
                Toast.makeText(getApplicationContext(), ((TextView) view).getText(), Toast.LENGTH_SHORT).show();

            }
        });

        registerForContextMenu(lv);

    }
    public String Check_sd(){
        String final_data="";

        try{
            File sdcard = Environment.getExternalStorageDirectory();
            File directory = new File(sdcard.getAbsolutePath()+ "/MyDirectory");
            File file = new File(directory,"textfile.txt");
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            char[] data = new char[data_block];

            int size;
            try{
                while((size=isr.read(data))>0){
                    String read_data = String.copyValueOf(data, 0, size);
                    final_data+= read_data;
                    data = new char[data_block];

                }

            }catch (IOException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
            }

        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return final_data;

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //adapt listview contents
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        String tem = colorV[info.position];
        sdw=tem;
        //sdw=myColor;
        menu.setHeaderTitle("Select The Action");
        menu.add(0, v.getId(), 0, "Write Color");
        menu.add(0, v.getId(), 0, "Read Color");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Write Color") {
            //sdw=myColor;

            try {

                File sdcard = Environment.getExternalStorageDirectory();
                File directory = new File(sdcard.getAbsolutePath() + "/MyDirectory");
                directory.mkdirs();
                File file = new File(directory, "textfile.txt");
                FileOutputStream fou = new FileOutputStream(file);

                OutputStreamWriter osw = new OutputStreamWriter(fou);
                try {
                    osw.write(sdw);
                    osw.flush();
                    osw.close();
                    Toast.makeText(getApplicationContext(), "Done writing SD "+sdw, Toast.LENGTH_SHORT).show();

                } catch(IOException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
            catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }


        }else if (item.getTitle() == "Read Color") {


            try{
                File sdcard = Environment.getExternalStorageDirectory();
                File directory = new File(sdcard.getAbsolutePath()+ "/MyDirectory");
                File file = new File(directory,"textfile.txt");
                FileInputStream fis = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(fis);
                char[] data = new char[data_block];
                String final_data="";
                int size;
                try{
                    while((size=isr.read(data))>0){
                        String read_data = String.copyValueOf(data, 0, size);
                        final_data+= read_data;
                        data = new char[data_block];

                    }
                    final_data=final_data.substring(2);
                    back.setBackgroundColor(Color.parseColor("#"+final_data));
                    Toast.makeText(getBaseContext(), "Message :"+final_data, Toast.LENGTH_LONG).show();
                }catch (IOException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }
            catch (Exception e)
            {
                Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
            }


        } else {
            return false;
        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}