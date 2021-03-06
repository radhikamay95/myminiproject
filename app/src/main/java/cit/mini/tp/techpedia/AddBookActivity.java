package cit.mini.tp.techpedia;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class AddBookActivity extends AppCompatActivity {

    EditText Bname, Bowner, Bauthor, Bprice, Bedpub, Baccessories, Bcontact, Bremark, Bdate;

    Button Sell;
    private FirebaseAuth mAuth;
    DatabaseReference databaseBook = FirebaseDatabase.getInstance().getReference("SellBooks");
    private DatePickerDialog fromDatePickerDialog;


    private SimpleDateFormat dateFormatter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);


        Bname = (EditText) findViewById(R.id.etbname);
        Bowner = (EditText) findViewById(R.id.etowner);
        Bauthor = (EditText) findViewById(R.id.etbauthor);
        Bprice = (EditText) findViewById(R.id.etbprice);
        Bedpub = (EditText) findViewById(R.id.etbEditionPublisher);
        Baccessories = (EditText) findViewById(R.id.etbAccessories);
        Bremark = (EditText) findViewById(R.id.etbRemarks);
        Bcontact = (EditText) findViewById(R.id.etbcontact);
        Bdate=(EditText)findViewById(R.id.etdate) ;

        Sell = (Button) findViewById(R.id.btsell);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);




       /* database = database.child(sellboookinfo.getUid());

        final SellBook sellBook = new SellBook();
        sellBook.setName(name);
        sellBook.setLast_name(lastName);
        sellBook.setEmail(email);
        sellBook.setMobile_number(mobileNumber);
        database.setValue(sellBook);*/
        Sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBook();
                Intent intent = new Intent(AddBookActivity.this, BookListActivity.class);
                startActivity(intent);

            }
        });
        Bdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDateTimeField();
                if (view == Bdate) {
                    fromDatePickerDialog.show();
                }

            }

        });
    }


     /*
    * This method is saving a new artist to the
    * Firebase Realtime Database
    * */


    private void addBook() {
        //getting the values to save
        String name = Bname.getText().toString().trim();
        String owner = Bowner.getText().toString().trim();
        String author = Bauthor.getText().toString().trim();
        String price = Bprice.getText().toString().trim();
        String edpub = Bedpub.getText().toString().trim();
        String accessories = Baccessories.getText().toString().trim();
        String contact = Bcontact.getText().toString().trim();
        String remark = Bremark.getText().toString().trim();
        String date = Bdate.getText().toString().trim();


        //checking if the value is provided
        if (!TextUtils.isEmpty(name)) {

            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our Artist
            String id = databaseBook.push().getKey();

            //creating an Artist Object
            SellBook sellBook = new SellBook(id, name, owner, author, edpub, price, accessories, contact, remark,date);

            //Saving the Artist
            databaseBook.child(id).setValue(sellBook);

            //setting edittext to blank again
            Bname.setText("");
            Bowner.setText("");
            Bauthor.setText("");
            Bprice.setText("");
            Bedpub.setText("");
            Baccessories.setText("");
            Bcontact.setText("");
            Bremark.setText("");
            Bdate.setText("");

            //displaying a success toast
            Toast.makeText(this, "Book Info added", Toast.LENGTH_LONG).show();
        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter a Book Name", Toast.LENGTH_LONG).show();
        }
    }



    private void setDateTimeField() {

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                Bdate.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));



    }


}

