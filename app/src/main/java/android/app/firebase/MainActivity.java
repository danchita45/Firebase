package android.app.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.model.Persona;
import android.os.Bundle;
import android.os.strictmode.IncorrectContextUseViolation;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    EditText Nombre,Apellido,Correo,Contraseña;
    ListView ListPersonas;
    FirebaseDatabase  firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<Persona> personas = new ArrayList<Persona>();
    ArrayAdapter<Persona> arrayAdapterPersona;
    Persona personasSelected;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Nombre = findViewById(R.id.txtNombrePersona);
        Apellido = findViewById(R.id.txtApellidoPersona);
        Correo = findViewById(R.id.txtCorreo);
        Contraseña = findViewById(R.id.txtContraseña);
         ListPersonas = findViewById(R.id.LV_datosPersonas);

         inicializarfirebase();
         listarDatos();

         ListPersonas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                 personasSelected = (Persona) adapterView.getItemAtPosition(i);
                 Nombre.setText(personasSelected.getNombre());
                 Apellido.setText(personasSelected.getApellido());
                 Correo.setText(personasSelected.getCorreo());
                 Contraseña.setText(personasSelected.getPassword());
             }
         });
    }

    private void inicializarfirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
    private void listarDatos() {
        databaseReference.child("Persona").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                personas.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Persona persona = data.getValue(Persona.class);
                    personas.add(persona);
                    //arrayAdapterPersona = new ArrayAdapter<Persona>(MainActivity.this, android.R.layout.simple_list_item_1, listaP);
                    arrayAdapterPersona = new ArrayAdapter<Persona>(MainActivity.this, android.R.layout.simple_list_item_1, personas);
                    ListPersonas.setAdapter(arrayAdapterPersona);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

String nombre = Nombre.getText().toString();
String apellido = Apellido.getText().toString();
String correo = Correo.getText().toString();
String contraseña = Contraseña.getText().toString();

        switch (item.getItemId()){
            case R.id.icon_add:{
                Persona p = new Persona();
                p.setUid(UUID.randomUUID().toString());
                p.setNombre(nombre);
                p.setApellido(apellido);
                p.setCorreo(correo);
                p.setPassword(contraseña);
                databaseReference.child("Persona").child(p.getUid()).setValue(p);
                Toast.makeText(this,"Agregar",Toast.LENGTH_SHORT);
                Limpia();

                break;
            }

            case R.id.icon_save:{
                Persona p = new Persona();
                p.setUid(UUID.randomUUID().toString());
                p.setNombre(nombre);
                p.setApellido(apellido);
                p.setCorreo(correo);
                p.setPassword(contraseña);
                databaseReference.child("Persona").child(p.getUid()).setValue(p);
                Toast.makeText(this,"Guardar",Toast.LENGTH_SHORT);
                break;
            }
            case R.id.icon_delete:{
                Persona p = new Persona();
                p.setUid(personasSelected.toString());
                databaseReference.child("Persona").child(p.getUid()).removeValue();
                Toast.makeText(this,"Eliminar",Toast.LENGTH_SHORT);
                break;
            }
            default: break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void Limpia(){
        Nombre.setText("");
        Apellido.setText("");
        Correo.setText("");
        Contraseña.setText("");
    }
}