package br.senai.sp.agendaformativa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import br.senai.sp.dao.ContatoDAO;
import br.senai.sp.modelo.Contatos;

public class CadastroContatoActivity extends AppCompatActivity {

    private CadastroContatoHelper helper;
    private int idCadastro = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_contato);

        helper = new CadastroContatoHelper(this);

        Intent intent = getIntent();
        Contatos contato = (Contatos) intent.getSerializableExtra("contato");

        if (contato != null){
            idCadastro = contato.getId();
            helper.preencherFormulario(contato);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_cadastro_contato, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        helper = new CadastroContatoHelper(this);
        ContatoDAO dao = new ContatoDAO(this);
        boolean temErros = helper.validacao();

        if(temErros){
            Toast.makeText(CadastroContatoActivity.this,"Não foi possivel salvar", Toast.LENGTH_LONG).show();
        }

        switch (item.getItemId()){
            case R.id.acao_salvar:

                Contatos contato = helper.getContato();
                contato.setId(idCadastro);

                if(contato.getId() == 0 && !temErros){
                    Toast.makeText(this, contato.getNome() + " salvo com sucesso", Toast.LENGTH_LONG).show();
                    dao.salvar(contato);
                    dao.close();
                    finish();
                }else if(!temErros){
                    Toast.makeText(this, contato.getNome() + " atualizado com sucesso", Toast.LENGTH_LONG).show();
                    dao.atualizar(contato);
                    dao.close();
                    finish();
                }
        }

        return super.onOptionsItemSelected(item);
    }
}
