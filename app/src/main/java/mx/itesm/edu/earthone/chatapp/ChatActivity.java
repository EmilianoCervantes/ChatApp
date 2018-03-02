package mx.itesm.edu.earthone.chatapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.data.client.AuthUiInitProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

public class ChatActivity extends Activity {

    private Button button;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    //Una bandera
    private final int LOGIN = 123;

    private Button logOut;

    private EditText editText;
    private ListView listView;
    private ChatAdapter adapter;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //Ya esta configurado, nada mas digo que quiero una referencia
        firebaseDatabase = FirebaseDatabase.getInstance();
        //Nos va a servir para hacer lo que queramos con la BD
        //Se agregara un nodo principal y a ese le pegaremos los mensajes
        databaseReference = firebaseDatabase.getReference().child("messages");

        listView = (ListView)findViewById(R.id.listView);
        adapter = new ChatAdapter(this, R.layout.chat_layout, new ArrayList<ChatPojo>());

        logOut = (Button)findViewById(R.id.logOut);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthUI.getInstance().signOut(getApplicationContext());
            }
        });

        editText = (EditText)findViewById(R.id.editText2);
        button = (Button)findViewById(R.id.button2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                String string = sharedPreferences.getString("name", "not saved");
                */

                //Lo usamos ahora con el authStateListener
                String message = editText.getText().toString();
                //Aqui estan harcoded los datos
                ChatPojo chatPojo = new ChatPojo(name, null, message);
                //Subir un dato
                databaseReference.push().setValue(chatPojo);
                //Necesitamos la referencia al EditText
                editText.setText("");
            }
        });

        //Va a cachar si se logeo o no
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null){
                    //
                    Toast.makeText(getApplicationContext(), "Bienvenido", Toast.LENGTH_LONG).show();
                    name = firebaseUser.getDisplayName();
                } else {
                    //Si no se logeo, se muestre la lista de logins
                    //La lista de proveedores (osea, redes sociales, etc)
                    startActivityForResult(
                            AuthUI
                            .getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.EmailBuilder().build(),
                                    new AuthUI.IdpConfig.GoogleBuilder().build()
                                    )
                            )
                            .build(),
                            LOGIN
                    );
                }
            }
        };

        loadChats();
    }

    private void clean(){
        name = "";
        adapter.clear();
        if (childEventListener != null) {
            databaseReference.removeEventListener(childEventListener);
            childEventListener = null;
        }
    }

    private void loadChats(){
        //Firebase tiene un listener para los hijos o los objs en la base
        childEventListener = new ChildEventListener() {
            //datasnapshot es el cambio en la base
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //ChatPojo.class lo mapea y nos da la referencia
                //No hay que poner gets y sets
                ChatPojo chatPojo = dataSnapshot.getValue(ChatPojo.class);
                adapter.add(chatPojo);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            //Mover en cuanto a orden
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        //Rederencia a la base de datos
        //Esta ultima linea es porque creamos el adapter pero no estaba escuchando nada
        databaseReference.addChildEventListener(childEventListener);
    }

    //Quitarlo porque se va a salir de la app o la actividad
    @Override
    protected void onPause() {
        super.onPause();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (firebaseAuth != null){
            firebaseAuth.addAuthStateListener(authStateListener);
        }
    }
}
