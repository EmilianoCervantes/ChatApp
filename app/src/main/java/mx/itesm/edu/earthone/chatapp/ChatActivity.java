package mx.itesm.edu.earthone.chatapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatActivity extends Activity {

    private Button button;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //Ya esta configurado, nada mas digo que quiero una referencia
        firebaseDatabase = FirebaseDatabase.getInstance();
        //Nos va a servir para hacer lo que queramos con la BD
        //Se agregara un nodo principal y a ese le pegaremos los mensajes
        databaseReference = firebaseDatabase.getReference().child("messages");
        editText = (EditText)findViewById(R.id.editText2);
        button = (Button)findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = editText.getText().toString();
                //Aqui estan harcoded los datos
                ChatPojo chatPojo = new ChatPojo("Emiliano", null, message);
                //Subir un dato
                databaseReference.push().setValue(chatPojo);
                //Necesitamos la referencia al EditText
                editText.setText("");
            }
        });
    }
}
