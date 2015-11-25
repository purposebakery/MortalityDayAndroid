package de.techlung.android.mortalityday;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import de.techlung.android.mortalityday.settings.PreferencesActivity;

public class MessageActivity extends BaseActivity {
    public static final String MESSAGE_EXTRA = "MESSAGE_EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_activity);

        String message = getIntent().getStringExtra(MESSAGE_EXTRA);
        TextView text = (TextView) findViewById(R.id.message);
        text.setText(message);

        findViewById(R.id.message_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MessageActivity.this, PreferencesActivity.class);
                startActivity(intent);
            }
        });
    }

}
