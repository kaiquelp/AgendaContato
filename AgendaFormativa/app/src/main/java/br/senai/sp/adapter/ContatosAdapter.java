package br.senai.sp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import br.senai.sp.agendacontatos.R;
import br.senai.sp.conversores.Imagem;
import br.senai.sp.modelo.Contato;

public class ContatosAdapter extends BaseAdapter {

    private List<Contato> contatos;
    private Context context;

    public ContatosAdapter(Context context, List<Contato> contatos){
        this.contatos = contatos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return contatos.size();
    }

    @Override
    public Object getItem(int position) {
        return contatos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return contatos.get(position).getCod();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Contato contato = contatos.get(position);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.lyt_lista_contatos, null);
        TextView txtNome = view.findViewById(R.id.txt_nome);
        txtNome.setText(contato.getNome());
        TextView txtTelefone = view.findViewById(R.id.txt_telefone);
        txtTelefone.setText(contato.getTelefone());
        ImageView foto = view.findViewById(R.id.img_contato);
        foto.setImageBitmap(Imagem.arrayToBitmap(contato.getFoto()));

        return view;
    }

}