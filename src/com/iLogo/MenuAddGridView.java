package com.iLogo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;

import com.iLogo.R;

public class MenuAddGridView extends Activity {
    GridView gridview;
    SimpleAdapter gridviewAdapter;
    ImageButton openFolder,addSingle,backFolder;
    String sdcardFilePath,thisFilePath,selectFilePath;
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.setContentView(R.layout.menuaddgridview);
        
        sdcardFilePath=Environment.getExternalStorageDirectory().getAbsolutePath();//得到sdcard目录
        thisFilePath=sdcardFilePath;
        
        openFolder=(ImageButton) this.findViewById(R.id.MenuAddGridView_button_openfolder);
        openFolder.setVisibility(View.INVISIBLE);//设置不可见
        openFolder.setOnClickListener(new buttonOnClickListener());//添加监听器
        addSingle=(ImageButton) this.findViewById(R.id.MenuAddGridView_button_addSingle);
        addSingle.setVisibility(View.INVISIBLE);
        addSingle.setOnClickListener(new buttonOnClickListener());
        backFolder=(ImageButton) this.findViewById(R.id.MenuAddGridView_button_backFolder);
        backFolder.setOnClickListener(new buttonOnClickListener());
        
        gridview=(GridView) this.findViewById(R.id.MenuAddGridView_gridview);
        //设置gridView的数据
        updategridviewAdapter(thisFilePath);
        gridview.setOnItemClickListener(new OnItemClickListener(){
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                HashMap<String, Object> item=(HashMap<String, Object>) arg0.getItemAtPosition(arg2);
                selectFilePath=(String) item.get("ItemFilePath");
                
                if(item.get("type").equals("isDirectory"))//判断是否是文件夹
                {
                    openFolder.setVisibility(View.VISIBLE);//打开文件按钮可见
                    addSingle.setVisibility(View.INVISIBLE);//选择单曲按钮不可见
                }
                else if(item.get("type").equals("isLogo"))
                {
                    openFolder.setVisibility(View.INVISIBLE);
                    addSingle.setVisibility(View.VISIBLE);
                }
                else
                {
                    openFolder.setVisibility(View.INVISIBLE);
                    addSingle.setVisibility(View.INVISIBLE);
                }
                
            }});
            this.setResult(0);
    }
    private File[] folderScan(String path)
    {
        File file=new File(path);
        File[] files=file.listFiles();
        return files;
    }
    //设置gridView的数据
    private void updategridviewAdapter(String filePath)
    {
        File[] files=folderScan(filePath);
        ArrayList<HashMap<String, Object>> lstImageItem = getFileItems(files);
        gridviewAdapter = new SimpleAdapter(MenuAddGridView.this,lstImageItem,R.layout.menuaddgridview_item,new String[] {"ItemImage","ItemText"}, new int[] {R.id.MenuAddGridView_ItemImage,R.id.MenuAddGridView_ItemText});
        gridview.setAdapter(gridviewAdapter);
        gridviewAdapter.notifyDataSetChanged();
    }
    //列表循环判断文件类型然后提供数据给Adapter用
    private ArrayList<HashMap<String, Object>> getFileItems(File[] files)
    {
        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
        //循环加入listImageItem数据
        if(files==null)
        {
            return null;
        }
        for(int i=0;i<files.length;i++)
        {
            HashMap<String, Object> map = new HashMap<String, Object>();
            String fileName=files[i].getName();//得到file名
            map.put("ItemText", fileName);
            if(files[i].isDirectory())//判断是否是文件夹
            {
                map.put("ItemImage", R.drawable.live_folder_notes);//显示文件夹图标
                map.put("type", "isDirectory");
            }
            else if(files[i].isFile())//判断是否是文件
            {
                if(fileName.contains(".logo"))//判断是否是LOGO文件
                {
                    map.put("ItemImage", R.drawable.app_notes);
                    map.put("type", "isLogo");
                }
                else
                {
                    map.put("ItemImage", R.drawable.app_notes);
                    map.put("type", "isOthers");
                }
            }
            map.put("ItemFilePath", files[i].getAbsolutePath());//保存文件绝对路径
            
            lstImageItem.add(map);
        }
        return lstImageItem;
    }
    class buttonOnClickListener implements OnClickListener
    {
        ArrayList<String> musicResult;
        Intent intent;
        public void onClick(View v) {
            switch(v.getId())
            {
                case R.id.MenuAddGridView_button_openfolder://打开文件夹
                    updategridviewAdapter(selectFilePath);//获取文件夹下数据并显示
                    thisFilePath=selectFilePath;//记录当前目录路径
                    openFolder.setVisibility(View.INVISIBLE);
                    addSingle.setVisibility(View.INVISIBLE);
                    break;
                case R.id.MenuAddGridView_button_addSingle:
                	SharedPreferences preferences = getSharedPreferences("name", MODE_PRIVATE); 
                	Editor editor = preferences.edit(); 
                	editor.putString("logo", selectFilePath); 
                	editor.commit(); 
                    MenuAddGridView.this.onDestroy();
                    break;
                case R.id.MenuAddGridView_button_backFolder://返回上级目录
                    if(!thisFilePath.equals(sdcardFilePath))
                    {
                        File thisFile=new File(thisFilePath);//得到当前目录
                        String parentFilePath=thisFile.getParent();//上级的目录路径
                        updategridviewAdapter(parentFilePath);//获得上级目录数据并显示
                        thisFilePath=parentFilePath;//设置当前目录路径
                        
                        openFolder.setVisibility(View.INVISIBLE);
                        addSingle.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        MenuAddGridView.this.onDestroy();
                    }
                    break;
            }
        }
        
    }
    protected void onDestroy() {
        this.finish();
        super.onDestroy();
    }
    
}
