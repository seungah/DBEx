package starbloom0128.kr.hs.emirim.dbex;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText editName, editCount, editResultName, editResultCount;
    Button butInit, butInsert, butSelect, butUpdate;
    MyDBHelper myHelper;
    SQLiteDatabase sqlDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editName=(EditText)findViewById(R.id.edit_group_name);
        editCount=(EditText)findViewById(R.id.edit_group_cnt);
        editResultName=(EditText)findViewById(R.id.edit_result_name);
        editResultCount=(EditText)findViewById(R.id.edit_result_cnt);
        butInit=(Button)findViewById(R.id.but_init);
        butInsert=(Button)findViewById(R.id.but_insert);
        butSelect=(Button)findViewById(R.id.but_select);
        butUpdate=(Button)findViewById(R.id.but_update);

        //DB생성
        myHelper=new MyDBHelper(this);
        //기존의 테이블이 존재하면 삭제하고 테이블을 새로 생성한다.
        butInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlDb = myHelper.getWritableDatabase();
                myHelper.onUpgrade(sqlDb, 1,2);
                sqlDb.close();
            }
        });
        butInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlDb=myHelper.getWritableDatabase();
                String sql="insert into idolTable values('"+editName.getText()+"', "+editCount.getText()+")"; //문장을 연결시키기 위한것!
                sqlDb.execSQL(sql);
                sqlDb.close();
                Toast.makeText(MainActivity.this, "저장됨", Toast.LENGTH_LONG).show(); //여기까지 저장이 잘 되었음을 의미
            }
        });
        butSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlDb=myHelper.getReadableDatabase();
                String sql="select * from idolTable";
                Cursor cursor = sqlDb.rawQuery(sql, null);
                String names = "Idol 이름" + "\r\n"+"=============="+"\r\n";
                String counts = "Idol 이름" + "\r\n"+"=============="+"\r\n";
                while (cursor.moveToNext()){
                    names += cursor.getString(0)+"\r\n";
                    counts += cursor.getInt(1)+"\r\n"; //연산이 필요할 때 정수형 값 그대로를 쓰기 위해, getInt로 반환받을 수 있음.
                }
                editResultName.setText(names);
                editResultCount.setText(counts);
                cursor.close();
                sqlDb.close();

            }
        });
        butUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlDb=myHelper.getWritableDatabase();
                String sql="update idolTable set idolCount=editCount=" + editCount.getText() + " where idolName='"+editName.getText()+"'";
                sqlDb.execSQL(sql);
                sqlDb.close();
                Toast.makeText(MainActivity.this, "인원수가 수정됨", Toast.LENGTH_LONG).show(); //여기까지 저장이 잘 되었음을 의미
            }
        });
    }
    class MyDBHelper extends SQLiteOpenHelper{//추상 클래스
        // idolDB라는 이름의 데이터베이스가 생성된다.

        public MyDBHelper(Context context) {
            super(context, "idolDB", null, 1);
        }
        //idolTable이라는 이름의 테이블 생성

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            String sql="create table idolTable(idolname text not null primary key, idolCount integer)";
            sqLiteDatabase.execSQL(sql);
        }
        //이미 idolTable이 존재한다면 기존의 테이블을 삭제하고 새로 테이블 만들 때 호출
        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            String sql="drop table if exists idolTable";
            sqLiteDatabase.execSQL(sql);
            onCreate(sqLiteDatabase);
        }
    }
}
