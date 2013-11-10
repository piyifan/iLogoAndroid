package com.iLogo;

import java.util.ArrayList;

import com.iLogo.draw.Drawer;
import com.iLogo.driver.Driver;
import com.iLogo.iCode.Functy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class MainWindowActivity extends Activity {
    /** Called when the activity is first created. */
	Activity nowActivity;
	String fileNameString;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	nowActivity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        LinearLayout container = (LinearLayout) findViewById(R.id.container);
        Button compileButton = (Button)findViewById(R.id.Compile);
        Button fileButton = (Button)findViewById(R.id.File);
        Button aboutButton = (Button)findViewById(R.id.About);
        Button clearButton = (Button)findViewById(R.id.Clear);
        
        final Drawer draw = new Drawer(this);
        Driver.setDriver();
        draw.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
        		LinearLayout.LayoutParams.WRAP_CONTENT));
        container.addView(draw);
        
        
        compileButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
			   SharedPreferences preferences = getSharedPreferences("name", MODE_PRIVATE); 
			   fileNameString = preferences.getString("logo", null); 
			   ArrayList<Functy> functys = 
					   Driver.getDriver().run(fileNameString, nowActivity);
			   if (functys == null) return;
		       draw.setFunctys( functys );
			   Drawer.FunctyInterpreter run = 
		    		   draw.new FunctyInterpreter(draw.functys.get(0), null, 0);
		       run.visitFuncty();
			}
		});
        
        fileButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent intent =new Intent(MainWindowActivity.this,MenuAddGridView.class);
				 startActivity(intent); 
			}
		});
        
        clearButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 draw.clearDraw();
			}
		});
        
        aboutButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				new AlertDialog.Builder(nowActivity).setTitle("About")
				.setMessage("iLogo Moblie Picture Language\n" +
						"Version: 1.0\n" +
						"Developer: Yifan Pi\n" +
						"IIIS,Tsinghua University\n" +
						"Email: piyifan@gmail.com")
				.setPositiveButton("Confirm", null).show();
			}
		});
        
    }
}