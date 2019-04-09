package br.senai.sp.agendacontatos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import br.senai.sp.dao.ContatoDAO;
import br.senai.sp.modelo.Contato;

public class CadastroContatoActivity extends AppCompatActivity{

    private Contato contatoAtualizar;

    private CadastroContatoHelper helper;

    private ImageButton btnCamera;
    private ImageButton btnGaleria;
    private ImageView imgContato;

    public static final int GALERIA_REQUEST = 4002;
    public static final int CAMERA_REQUEST = 8922;

    private String caminhoFoto;

    // No estado onCreate() decide se irá preencher a tela de cadastro com um item que foi clicado anteriormente ou não
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_contato);

        helper = new CadastroContatoHelper(this);

        btnCamera = findViewById(R.id.btn_camera);
        btnGaleria = findViewById(R.id.btn_galeria);
        imgContato = findViewById(R.id.img_contato_cadastro);

        btnGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGaleria = new Intent (Intent.ACTION_GET_CONTENT);
                intentGaleria.setType("image/*");
                startActivityForResult(intentGaleria, GALERIA_REQUEST);
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                String nomeArquivo = "/IMG_" + System.currentTimeMillis() + ".png";
                caminhoFoto = getExternalFilesDir(null) + nomeArquivo;
                File arquivoFoto = new File(caminhoFoto);

                Uri fotoUri = FileProvider.getUriForFile(CadastroContatoActivity.this, BuildConfig.APPLICATION_ID + ".provider", arquivoFoto);
                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri);
                startActivityForResult(intentCamera, CAMERA_REQUEST);
            }
        });

        Intent intent = getIntent();
        contatoAtualizar = (Contato)intent.getSerializableExtra("contato");
        // Se o contato estiver com valores quando a tela de cadastro for criada, então deve ser preenchida a tela com as infos do contato em questão
        if(contatoAtualizar != null){
            helper.preencherFormulario(contatoAtualizar);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode == RESULT_OK){

            try {

                if(requestCode == GALERIA_REQUEST){
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 500, 500, true);
                    imgContato.setImageBitmap(bitmapReduzido);
                    imgContato.setScaleType(ImageView.ScaleType.FIT_XY);
                }

                if(requestCode == CAMERA_REQUEST){
                    Bitmap bitmap = BitmapFactory.decodeFile(caminhoFoto);
                    Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 500, 500, true);
                    imgContato.setImageBitmap(bitmapReduzido);
                    imgContato.setScaleType(ImageView.ScaleType.FIT_XY);
                }

            }catch(FileNotFoundException e){
                e.printStackTrace();
            }
        }

    }

    // Adiciona o menu com o botão salvar na activity de cadastro de contatos
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_cadastro_contatos, menu);

        return super.onCreateOptionsMenu(menu);
    }

    // Ao selecionar o botão "Salvar" executa o comando necessário
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_salvar:
                Contato contato = helper.getContato();
                ContatoDAO dao = new ContatoDAO(this);

                // Se o contato for nulo, ele não existe, e é então executado o comando de salvar, senão, ele atualiza o mesmo cadastro
                if(contatoAtualizar == null){

                    if (helper.validacao()) {
                        dao.salvar(contato);
                        dao.close();
                        finish();
                        Toast.makeText(this, contato.getNome() + " salvo com sucesso", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    int cod = contatoAtualizar.getCod();
                    contatoAtualizar = helper.getContato();
                    contatoAtualizar.setCod(cod);

                    if(helper.validacao()){
                        dao.atualizar(contatoAtualizar);
                        dao.close();
                        finish();
                        Toast.makeText(this, contatoAtualizar.getNome() + " atualizado com sucesso", Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case R.id.menu_fone:
                Contato contatoLigacao = helper.getContato();

                if(contatoLigacao.getCod()==0){
                    Toast.makeText(this, contatoAtualizar.getNome() + "Cadastre o contato", Toast.LENGTH_SHORT).show();
                }else{
                    Contato contatoNum = helper.getContato();
                    Uri uri = Uri.parse("tel:" + contatoNum.getTelefone());

                    Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                    startActivity(intent);
                }

                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
