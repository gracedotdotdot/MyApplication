import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.user.myapplication.R;

public class battery extends AppCompatActivity {
    public ImageView BView;
    int electricity=50;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.battery_status);
        String full = "@drawable/battery_full";  // where myresource (without the extension) is the file
        String medium = "@drawable/battery_medium";
        String low = "@drawable/battery_low";
        TextView elec = findViewById(R.id.electricity);
        elec.setText(Integer.toString(electricity));

        if(electricity>70 && electricity!=100){
            int imageResource = getResources().getIdentifier(full, null, getPackageName());
            BView = findViewById(R.id.bview);
            Drawable res = getResources().getDrawable(imageResource);
            BView.setImageDrawable(res);
        }
        else if(electricity>20 && electricity<70){
            int imageResource = getResources().getIdentifier(medium, null, getPackageName());
            BView = findViewById(R.id.bview);
            Drawable res = getResources().getDrawable(imageResource);
            BView.setImageDrawable(res);
        }
        else if(electricity<20){
            int imageResource = getResources().getIdentifier(low, null, getPackageName());
            BView = findViewById(R.id.bview);
            Drawable res = getResources().getDrawable(imageResource);
            BView.setImageDrawable(res);
        }
        else if(electricity==100){
            int imageResource = getResources().getIdentifier(full, null, getPackageName());
            BView = findViewById(R.id.bview);
            Drawable res = getResources().getDrawable(imageResource);
            BView.setImageDrawable(res);
        }

    }

}
