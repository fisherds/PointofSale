package edu.rosehulman.fisher.pointofsale;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
  private Item mCurrentItem;
  private TextView mNameTextView;
  private TextView mQuantityTextView;
  private TextView mDateTextView;
  private Item mClearedItem;
  private ArrayList<Item> mItems;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    mNameTextView = (TextView) findViewById(R.id.name_text);
    mQuantityTextView = (TextView) findViewById(R.id.quantity_text);
    mDateTextView = (TextView) findViewById(R.id.date_text);

    registerForContextMenu(mNameTextView);
    mItems = new ArrayList<>();


    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
//        mCurrentItem = Item.getDefaultItem();
//        Log.d(Constants.TAG, "You clicked the button.");
//        showCurrentItem();
        addEditItem(false);

      }
    });
  }

  private void addEditItem(final boolean isEditing) {
    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
    String title = isEditing ? "Edit an Item" : "Add an Item";
    builder.setTitle(title);
    View view = getLayoutInflater().inflate(R.layout.dialog_add, null, false);
    builder.setView(view);
    final EditText nameEditText = (EditText) view.findViewById(R.id.edit_name);
    final EditText quantityEditText = (EditText) view.findViewById(R.id.edit_quantity);
    final CalendarView deliveryDateView = (CalendarView) view.findViewById(R.id.calendar_view);
    final GregorianCalendar calendar = new GregorianCalendar();

    if (isEditing) {
      nameEditText.setText(mCurrentItem.name);
      quantityEditText.setText(Integer.toString(mCurrentItem.quantity));
      deliveryDateView.setDate(mCurrentItem.getDeliveryDateTime());
    }

    deliveryDateView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

      public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
        calendar.set(year, month, dayOfMonth);
      }
    });
    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        String name = nameEditText.getText().toString();
        int quantity = Integer.parseInt(quantityEditText.getText().toString());
        if (isEditing) {
          mCurrentItem.name = name;
          mCurrentItem.quantity = quantity;
          mCurrentItem.deliveryDate = calendar;
        } else {
          mCurrentItem = new Item(name, quantity, calendar);
          mItems.add(mCurrentItem);
        }
        showCurrentItem();
      }
    });
    builder.setNegativeButton(android.R.string.cancel, null);
    builder.create().show();
  }

  private void showCurrentItem() {
    mNameTextView.setText(getString(R.string.name_format, mCurrentItem.name));
    mQuantityTextView.setText(getString(R.string.quantity_format, mCurrentItem.quantity));
    mDateTextView.setText(getString(R.string.date_format, mCurrentItem.getDeliveryDateString()));
  }

  @Override
  public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
    super.onCreateContextMenu(menu, v, menuInfo);
    getMenuInflater().inflate(R.menu.menu_context, menu);
  }

  @Override
  public boolean onContextItemSelected(MenuItem item) {
    int id = item.getItemId();
    switch (id) {
      case R.id.context_edit:
        addEditItem(true);
        Log.d(Constants.TAG, "You clicked on edit context menu");
        break;
      case R.id.context_remove:
        Log.d(Constants.TAG, "You clicked on remove context menu");
        removeItem();
        break;
    }
    return super.onContextItemSelected(item);
  }

  private void removeItem() {
    mItems.remove(mCurrentItem);
    mCurrentItem = new Item();
    showCurrentItem();
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
      case R.id.action_search:
        showSearchDialog();
        return true;
      case R.id.action_settings:
        startActivity(new Intent(Settings.ACTION_SETTINGS));
        return true;
      case R.id.action_reset:
        Log.d(Constants.TAG, "You clicked reset!");
        mClearedItem = mCurrentItem;
        mCurrentItem = new Item();
        mCurrentItem = new Item();
        showCurrentItem();
        Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout),
            "Item cleared", Snackbar.LENGTH_LONG)
            .setAction("Action", new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                mCurrentItem = mClearedItem;
                mClearedItem = null;
                showCurrentItem();
              }
            });
        snackbar.show();
        return true;
      case R.id.action_clear_all:
        Log.d(Constants.TAG, "You clicked clear all!");
        confirmationDialog();
        return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private void showSearchDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
    builder.setTitle("Choose an item");
    builder.setItems(getNames(), new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        mCurrentItem = mItems.get(which);
        showCurrentItem();
      }
    });
    builder.setNegativeButton(android.R.string.cancel, null);
    builder.create().show();
  }

  private String[] getNames() {
    String[] names = new String[mItems.size()];
    for (int i = 0; i < mItems.size(); i++) {
      names[i] = mItems.get(i).name;
    }
    return names;
  }

  private void confirmationDialog() {

    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
    builder.setTitle("Remove");
    View view = getLayoutInflater().inflate(R.layout.dialog_confirmation, null, false);
    builder.setView(view);

    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        mItems.clear();
        showCurrentItem();
      }
    });
    builder.setNegativeButton(android.R.string.cancel, null);
    builder.create().show();
  }
}
