package edu.rosehulman.fisher.pointofsale;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
  private Item mCurrentItem;
  private TextView mNameTextView;
  private TextView mQuantityTextView;
  private TextView mDateTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    mNameTextView = (TextView) findViewById(R.id.name_text);
    mQuantityTextView = (TextView) findViewById(R.id.quantity_text);
    mDateTextView = (TextView) findViewById(R.id.date_text);




    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
//        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//            .setAction("Action", null).show();
        mCurrentItem = Item.getDefaultItem();
        Log.d(Constants.TAG, "You clicked the button.");
        showCurrentItem();

      }
    });
  }

  private void showCurrentItem() {
    mNameTextView.setText(getString(R.string.name_format, mCurrentItem.name));
    mQuantityTextView.setText(getString(R.string.quantity_format, mCurrentItem.quantity));
    mDateTextView.setText(getString(R.string.date_format, mCurrentItem.getDeliveryDateString()));
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_settings:
        startActivity(new Intent(Settings.ACTION_SETTINGS));
        return true;
      case R.id.action_reset:
        Log.d(Constants.TAG, "You clicked reset!");
        mCurrentItem = new Item();
        showCurrentItem();
        return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
