package mmga.aptdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.HelloWorld;
import com.example.MmgaToast;

@HelloWorld
@MmgaToast("操老师")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivityToaster.toast(this);
    }
}
