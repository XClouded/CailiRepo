package com.tima.behaviouralanalysis;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v4.app.NavUtils;

public class MainActivity extends Activity {

	private Handler mHandler = new Handler();
	private Runnable run = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
    		Integer[] in = new Integer[4];
    		in[5] = Integer.valueOf(5);
		}
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
 //       BehaviouralAnalysis.getInstance(this).onError(this);
        
        Button bta = (Button) findViewById(R.id.gotoactivitytesta);
        bta.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MainActivity.this, ActivityTestA.class));
			}
		});
        
        Button btb = (Button) findViewById(R.id.gotoactivitytestb);
        btb.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MainActivity.this, ActivityTestB.class));
			}
		});
        
        Button bt1 = (Button)findViewById(R.id.bt1);
        bt1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BehaviouralAnalysis.getInstance(MainActivity.this).onEvent(MainActivity.this, "1");
			}
		});
        
        Button bt2 = (Button)findViewById(R.id.bt2);
        bt2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BehaviouralAnalysis.getInstance(MainActivity.this).onEvent(MainActivity.this, "1", "一生有你");
			}
		});
        
        Button bt3 = (Button)findViewById(R.id.bt3);
        bt3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BehaviouralAnalysis.getInstance(MainActivity.this).onEventDuration(MainActivity.this, "1", "一生有你", 60);
			}
		});
        
        Button bt4 = (Button)findViewById(R.id.bt4);
        bt4.setOnClickListener(new View.OnClickListener() {
        	
        	@Override
        	public void onClick(View v) {
        		// TODO Auto-generated method stub
        		BehaviouralAnalysis.getInstance(MainActivity.this).onEvent(MainActivity.this, "1", "一生有你", 3);
        	}
        });
        
        Button bt5 = (Button)findViewById(R.id.bt5);
        bt5.setOnClickListener(new View.OnClickListener() {
        	
        	@Override
        	public void onClick(View v) {
        		// TODO Auto-generated method stub
        		mHandler.post(run);
        	}
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public void onResume() {
    	super.onResume();
    	
    	BehaviouralAnalysis.getInstance(this).onResume(this);
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    	
    	BehaviouralAnalysis.getInstance(this).onPause(this);
    }
}
