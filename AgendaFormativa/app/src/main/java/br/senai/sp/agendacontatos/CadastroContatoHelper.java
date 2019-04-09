package br.senai.sp.agendacontatos;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.TextInputLayout;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

import br.senai.sp.conversores.Imagem;
import br.senai.sp.modelo.Contato;

public class CadastroContatoHelper {

    private EditText txtNome;
    private EditText txtEndereco;
    private EditText txtTelefone;
    private EditText txtEmail;
    private EditText txtLinkedin;
    private ImageView foto;

    private TextInputLayout lytNome;
    private TextInputLayout lytEndereco;
    private TextInputLayout lytEmail;
    private TextInputLayout lytTelefone;
    private TextInputLayout lytLinkedin;
    private Contato contato;

    // O método construtor responsável por atribuir o conteúdo dos campos na CadastroContatoActivity às variáveis.
    public CadastroContatoHelper(CadastroContatoActivity activity){
        txtNome = activity.findViewById(R.id.txt_nome);
        txtEndereco = activity.findViewById(R.id.txt_endereco);
        txtTelefone = activity.findViewById(R.id.txt_telefone);
        txtEmail = activity.findViewById(R.id.txt_email);
        txtLinkedin = activity.findViewById(R.id.txt_linkedin);
        foto = activity.findViewById(R.id.img_contato_cadastro);

        lytNome = activity.findViewById(R.id.layout_txt_nome);
        lytEndereco = activity.findViewById(R.id.layout_txt_endereco);
        lytEmail = activity.findViewById(R.id.layout_txt_email);
        lytTelefone = activity.findViewById(R.id.layout_txt_telefone);
        lytLinkedin = activity.findViewById(R.id.layout_txt_linkedin);
        contato = new Contato();

    }

    // Responsável por pegar os dados de um contato e colocar nos campos do CadastroContatoActivity
    public Contato getContato(){

        contato.setNome(txtNome.getText().toString());
        contato.setEndereco(txtEndereco.getText().toString());
        contato.setTelefone(txtTelefone.getText().toString());
        contato.setEmail(txtEmail.getText().toString());
        contato.setLinkedin(txtLinkedin.getText().toString());

        Bitmap bm = ((BitmapDrawable)foto.getDrawable()).getBitmap();
        Bitmap bmReduzido = Bitmap.createScaledBitmap(bm, 500, 500, true);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmReduzido.compress(Bitmap.CompressFormat.PNG, 100, stream);

        byte[] fotoArray = stream.toByteArray();
        contato.setFoto(fotoArray);

        return contato;
    }

    // Responsável por preencher o formulário da CadastroContatoActivity caso o usuário tenha clicado em algum contato da lista
    public void preencherFormulario(Contato contato){
        txtNome.setText(contato.getNome());
        txtEndereco.setText(contato.getEndereco());
        txtEmail.setText(contato.getEmail());
        txtTelefone.setText(contato.getTelefone());
        txtLinkedin.setText(contato.getLinkedin());
        this.contato = contato;

        if(contato.getFoto() != null){
            foto.setImageBitmap(Imagem.arrayToBitmap(contato.getFoto()));
            foto.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        this.contato = contato;
    }

    // Responsável pela validação dos campos na activity de cadastro
    public boolean validacao(){

        String nome = txtNome.getText().toString();
        String endereco = txtEndereco.getText().toString();
        String email = txtEmail.getText().toString();
        String telefone = txtTelefone.getText().toString();
        String linkedin = txtLinkedin.getText().toString();

        boolean validar = true;

        if(nome.matches("")){
            lytNome.setErrorEnabled(true);
            lytNome.setError("Digite o nome");
            validar = false;
        }else{
            lytNome.setErrorEnabled(false);
        }

        if(endereco.matches("")){
            lytEndereco.setErrorEnabled(true);
            lytEndereco.setError("Digite o Endereço");
            validar = false;
        }else{
            lytEndereco.setErrorEnabled(false);
        }

        if(email.matches("")){
            lytEmail.setErrorEnabled(true);
            lytEmail.setError("Digite o Email");
            validar = false;
        }else{
            lytEmail.setErrorEnabled(false);
        }

        if(telefone.matches("")){
            lytTelefone.setErrorEnabled(true);
            lytTelefone.setError("Digite o Telefone");
            validar = false;
        }else{
            lytTelefone.setErrorEnabled(false);
        }

        if(linkedin.matches("")){
            lytLinkedin.setErrorEnabled(true);
            lytLinkedin.setError("Digite o Linkedin");
            validar = false;
        }else{
            lytLinkedin.setErrorEnabled(false);
        }

        return validar;
    }

}
