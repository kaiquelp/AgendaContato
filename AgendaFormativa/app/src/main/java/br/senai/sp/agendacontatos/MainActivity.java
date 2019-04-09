package br.senai.sp.agendacontatos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.List;

import br.senai.sp.adapter.ContatosAdapter;
import br.senai.sp.dao.ContatoDAO;
import br.senai.sp.modelo.Contato;

public class MainActivity extends AppCompatActivity {

    private ListView listaContatos;
    private ImageButton btnAdicionarContato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Cria a lista (atualizada do BD) e o botão para adicionar novos contatos
        listaContatos = findViewById(R.id.lista_contatos);
        btnAdicionarContato = findViewById(R.id.btn_adicionar);
        // Faz com que seja possível clicar no botão flutuante e ir para a tela de cadastro de contato
        btnAdicionarContato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cadastroContato = new Intent(MainActivity.this, CadastroContatoActivity.class);
                startActivity(cadastroContato);
            }
        });
        // Permite ter um menu de contexto para qualquer item da lista
        registerForContextMenu(listaContatos);
        // Abre a tela de cadastro porém com as informações do contato em questão (Apenas ao clicar e soltar)
        listaContatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contato contato = (Contato) listaContatos.getItemAtPosition(position);

                Intent cadastro = new Intent(MainActivity.this, CadastroContatoActivity.class);
                cadastro.putExtra("contato", contato);
                startActivity(cadastro);
            }
        });
    }
    // Coloca o menu personalizado no menu vazio da classe mãe, por meio do MenuInflater
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contexto_lista_contatos, menu);

        super.onCreateContextMenu(menu, v, menuInfo);
    }
    // Ao selecionar o item "Excluir" do menu de contexto mostra a caixa de confirmação
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final ContatoDAO dao = new ContatoDAO(this);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Contato contato = (Contato) listaContatos.getItemAtPosition(info.position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Excluir");
        builder.setMessage("Confirma a exclusão do contato " + contato.getNome() + "?");

        builder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dao.excluir(contato);
                dao.close();
                carregarLista();
            }
        });

        builder.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.create().show();

        return super.onContextItemSelected(item);
    }
    // No estado onResume() atualiza a lista de contatos novamente baseado no banco de dados
    protected void onResume () {
        carregarLista();
        super.onResume();
    }
    // O método responsável por atualizar a lista de contatos no estado onResume()
    private void carregarLista () {
        ContatoDAO dao = new ContatoDAO(this);
        List<Contato> contatos = dao.getContatos();
        dao.close();

        //ArrayAdapter<Contato> listaContatosAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contatos);
        ContatosAdapter adapter = new ContatosAdapter(this, contatos);
        listaContatos.setAdapter(adapter);
        //listaContatos.setAdapter(listaContatosAdapter);
    }

}
