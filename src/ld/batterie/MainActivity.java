package ld.batterie;

import android.os.BatteryManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView batteryInfo;
	private NotificationManager mgr = null;
	  private static final int NOTIFY_ME_ID=1337;
	  private int count=0;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Conteneur des infos batterie
		batteryInfo = (TextView)findViewById(R.id.textViewBatteryInfo);
		
		// Inscription aux changements d'états de la batterie
		this.registerReceiver(this.batteryInfoReceiver,	new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
	
		// Pour les notifications
		mgr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void envoi_notification(int level)
	{
		final class NotifyMessage extends Activity {
			  @Override
			  public void onCreate(Bundle savedInstanceState) {
			    super.onCreate(savedInstanceState);
			    
			    TextView txt=new TextView(this);
			    
			    txt.setText("This is the message!");
			    setContentView(txt);
			  }
			}
		Notification note = new Notification(R.drawable.ic_launcher, "Message", System.currentTimeMillis());
		 
		PendingIntent i=PendingIntent.getActivity(this, 0,
                 new Intent(this, NotifyMessage.class),
                                   0);

note.setLatestEventInfo(this, "Niveau batterie",
                 "This is the notification message", i);
note.number=++count;

mgr.notify(NOTIFY_ME_ID, note);
	}
	
	private String plugged_to_string(int id)
	{		
		String resultat = "?";
		switch(id)
		{
		case 0:
			resultat = "Aucun";
			break;
		case BatteryManager.BATTERY_PLUGGED_AC:
			resultat = "Secteur";
			break;
		case BatteryManager.BATTERY_PLUGGED_USB:
			resultat = "USB";
			break;
		case BatteryManager.BATTERY_PLUGGED_WIRELESS:
			resultat = "Sans fil";
			break;
		default:
			resultat = "?";
			break;
		}
		return resultat;
	}
	private String health_to_string(int id)
	{
		String resultat = "?";
		switch(id)
		{
		case BatteryManager.BATTERY_HEALTH_COLD:
			resultat = "Batterie froide";
			break;
		case BatteryManager.BATTERY_HEALTH_DEAD:
			resultat = "Batterie morte";
			break;
		case BatteryManager.BATTERY_HEALTH_GOOD:
			resultat = "Ok";
			break;
		case BatteryManager.BATTERY_HEALTH_OVERHEAT:
			resultat = "Batterie en surchauffe";
			break;
		case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
			resultat = "Batterie en surtension";
			break;
		case BatteryManager.BATTERY_HEALTH_UNKNOWN:
		case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
		default:
			resultat = "?";	
		}
		return resultat;
	}
	private String bool_to_string(Boolean bool)
	{
		String resultat = "Non";
		if(bool == true)
		{
			resultat = "Oui";
		}
		return resultat;
	}
	
	private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
		 @Override
		 public void onReceive(Context context, Intent intent) {
		 int health= intent.getIntExtra(BatteryManager.EXTRA_HEALTH,0);
		 //int icon_small= intent.getIntExtra(BatteryManager.EXTRA_ICON_SMALL,0);
		 int level= intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
		 int plugged= intent.getIntExtra(BatteryManager.EXTRA_PLUGGED,0);
		 boolean present= intent.getExtras().getBoolean(BatteryManager.EXTRA_PRESENT);
		 int scale= intent.getIntExtra(BatteryManager.EXTRA_SCALE,0);
		 int status= intent.getIntExtra(BatteryManager.EXTRA_STATUS,0);
		 String technology= intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);
		 int temperature= intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0);
		 int voltage= intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE,0);
		  
		 envoi_notification((int)((level/(float)scale)*100.0));
		 
		 batteryInfo.setText(
			 "Type			: "+technology+"\n"+
			 "Niveau charge : "+(int)((level/(float)scale)*100.0)+" %\n"+
			 "Etat          : "+status+"\n"+
			 "Chargeur      : "+plugged_to_string(plugged)+"\n"+
			 "Température   : "+temperature/10.0+" °C\n"+
			 "Tension		: "+voltage+" Volts\n"+
			 "Santé         : "+health_to_string(health)+"\n"+
			 "Présence      : "+bool_to_string(present)+"\n"
		 );
		 }
		 };	

}


