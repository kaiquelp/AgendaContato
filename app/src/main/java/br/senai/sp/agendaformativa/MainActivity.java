package br.senai.sp.agendaformativa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.senai.sp.dao.ContatoDAO;
import br.senai.sp.modelo.Contatos;

public class MainActivity extends AppCompatActivity {
    private ListView listaContatos;
    private Button btnNovoContato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listaContatos = findViewById(R.id.list_contatos);
        btnNovoContato = findViewById(R.id.btn_novo_contato);

        List<String> contatos = new ArrayList<>();
        contatos.add("Ale");
        contatos.add("Maria");
        contatos.add("Ricardo");
        contatos.add("Juaão");


        btnNovoContato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CadastroContatoActivity.class);
                startActivity(intent);
            }
        });

        registerForContextMenu(listaContatos);

        listaContatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contatos contato = (Contatos) listaContatos.getItemAtPosition(position);

                Intent cadastroContato = new Intent(MainActivity.this, CadastroContatoActivity.class);
                cadastroContato.putExtra("contato", contato);
                startActivity(cadastroContato);

            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_excluir_contato, menu);

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        final ContatoDAO dao = new ContatoDAO(this);
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final Contatos contato = (Contatos) listaContatos.getItemAtPosition(info.position);
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:

                        dao.excluir(contato);

                        dao.close();

                        carregarLista();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:

                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Você tem certeza que quer excluir " + contato.getNome() + "?").setPositiveButton("Sim", dialogClickListener)
                .setNegativeButton("Não", dialogClickListener).show();

        return super.onContextItemSelected(item);
    }

    private void carregarLista(){

        ContatoDAO dao = new ContatoDAO(this);
        List<Contatos> contatos = dao.getContatos();
        dao.close();

        ArrayAdapter<Contatos> listaFilmesAdapter = new ArrayAdapter<Contatos>(this, android.R.layout.simple_list_item_1, contatos);

        listaContatos.setAdapter(listaFilmesAdapter);
    }

    @Override
    protected void onResume() {
        carregarLista();
        super.onResume();
    }
}
