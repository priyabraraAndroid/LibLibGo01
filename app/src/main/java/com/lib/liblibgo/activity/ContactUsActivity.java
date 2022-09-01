package com.lib.liblibgo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lib.liblibgo.R;

public class ContactUsActivity extends AppCompatActivity {

    private TextView titleTool;
    private ImageView backTool;
    private EditText etEmail;
    private EditText etTitle;
    private EditText etmsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        titleTool = findViewById(R.id.titleTool);
        backTool = findViewById(R.id.backTool);
        setTopView(getString(R.string.contact_us));

        etEmail = (EditText)findViewById(R.id.etEmail);
        etTitle = (EditText)findViewById(R.id.etTitle);
        etmsg = (EditText)findViewById(R.id.etmsg);

    }

    private void setTopView(String string) {
        titleTool.setText(string);
        backTool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }

    public void onSubmit(View view) {
        String email = etEmail.getText().toString().trim();
        String title = etTitle.getText().toString().trim();
        String msg = etmsg.getText().toString().trim();

        if (email.equals("")){
            Toast.makeText(this, "Enter email id.", Toast.LENGTH_SHORT).show();
        }else if (title.equals("")){
            Toast.makeText(this, "Enter title.", Toast.LENGTH_SHORT).show();
        }else if (msg.equals("")){
            Toast.makeText(this, "Enter message.", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "We will send feadback in your given mail.", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }
}