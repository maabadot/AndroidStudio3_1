package com.example.practice3_1;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TableActivityV1 extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_v1);

        String tableID = "ID";
        String tableName = "Имя";
        String tableSurname = "Фамилия";
        String tableSecondName = "Отчество";
        String tableTime = "ВРЕМЯ";
        addRow(tableID, tableSurname, tableName, tableSecondName, tableTime);

        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_STUDENTS, null, null, null, null, null, null);

        if (cursor.moveToFirst())
        {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int surnameIndex = cursor.getColumnIndex(DBHelper.KEY_SURNAME);
            int secondnameIndex = cursor.getColumnIndex(DBHelper.KEY_SECONDNAME);
            int timeIndex = cursor.getColumnIndex(DBHelper.KEY_TIME);
            do {
                addRow(String.valueOf(cursor.getInt(idIndex)), cursor.getString(surnameIndex), cursor.getString(nameIndex), cursor.getString(secondnameIndex), cursor.getString(timeIndex));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public void addRow(String id, String name, String surname, String secondname,  String time)
    {
        //Сначала найдем в разметке активити саму таблицу по идентификатору
        TableLayout tableLayout = (TableLayout) findViewById(R.id.table);
        //Создаём экземпляр инфлейтера, который понадобится для создания строки таблицы из шаблона. В качестве контекста у нас используется сама активити
        LayoutInflater inflater = LayoutInflater.from(this);
        //Создаем строку таблицы, используя шаблон из файла /res/layout/table_row.xml
        TableRow tr = (TableRow) inflater.inflate(R.layout.tablerow_v1, null);

        TextView tv = (TextView) tr.findViewById(R.id.col1);
        tv.setText(id);
        tv = (TextView) tr.findViewById(R.id.col2);
        tv.setText(surname);
        tv = (TextView) tr.findViewById(R.id.col3);
        tv.setText(name);
        tv = (TextView) tr.findViewById(R.id.col4);
        tv.setText(secondname);
        tv = (TextView) tr.findViewById(R.id.col5);
        tv.setText(time);

        tableLayout.addView(tr); //добавляем созданную строку в таблицу
    }
}
