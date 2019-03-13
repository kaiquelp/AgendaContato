package br.senai.sp.agendaformativa;

import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

import br.senai.sp.modelo.Contatos;

public class CadastroContatoHelper {

    private TextInputLayout layoutTxtNome;
    private TextInputLayout layoutTxtEndereco;
    private TextInputLayout layoutTxtTelefone;
    private TextInputLayout layoutTxtEmail;
    private TextInputLayout layoutTxtLinkedin;

    private EditText txtNome;
    private EditText txtEndereco;
    private EditText txtTelefone;
    private EditText txtEmail;
    private EditText txtLinkedin;
    private Contatos contato;

    public CadastroContatoHelper(CadastroContatoActivity activity){
        txtNome = activity.findViewById(R.id.txt_nome_contato);
        txtEndereco = activity.findViewById(R.id.txt_endereco);
        txtTelefone = activity.findViewById(R.id.txt_telefone);
        txtEmail = activity.findViewById(R.id.txt_email);
        txtLinkedin = activity.findViewById(R.id.txt_linkedin);
        layoutTxtNome = activity.findViewById(R.id.layout_txt_nome);
        layoutTxtEndereco = activity.findViewById(R.id.layout_txt_endereco);
        layoutTxtTelefone = activity.findViewById(R.id.layout_telefone);
        layoutTxtEmail = activity.findViewById(R.id.layout_txt_email);
        layoutTxtLinkedin = activity.findViewById(R.id.layout_txt_linkedin);
        contato = new Contatos();
    }

    public Contatos getContato() {

        contato.setNome(txtNome.getText().toString());
        contato.setEndereco(txtEndereco.getText().toString());
        contato.setTelefone(txtTelefone.getText().toString());
        contato.setEmail(txtEmail.getText().toString());
        contato.setLinkedin(txtLinkedin.getText().toString());

        return contato;
    }

    public void preencherFormulario(Contatos contato){
        txtNome.setText(contato.getNome());
        txtEndereco.setText(contato.getEndereco());
        txtTelefone.setText(contato.getTelefone());
        txtEmail.setText(contato.getEmail());
        txtLinkedin.setText(contato.getLinkedin());

        this.contato = contato;
    }

    public boolean validacao (){
        String nome = txtNome.getText().toString();
        String endereco = txtEndereco.getText().toString();
        String email = txtEmail.getText().toString();
        String telefone = txtTelefone.getText().toString();
        String linkedin = txtLinkedin.getText().toString();

        boolean validar = false;

        if(nome.isEmpty()){
            layoutTxtNome.setError("Digite o seu nome");
            validar = true;
        }

        if(endereco.isEmpty()){
            layoutTxtEndereco.setError("Digite o seu endereço");
            validar = true;
        }

        if(email.isEmpty()){
            layoutTxtEmail.setError("Digite o seu email");
            validar = true;
        }

        if(telefone.isEmpty()){
            layoutTxtTelefone.setError("Digite o seu telefone");
            validar = true;
        }

        if(linkedin.isEmpty()){
            layoutTxtLinkedin.setError("Digite o seu linkedin");
            validar = true;
        }


        return validar;
    }

}
