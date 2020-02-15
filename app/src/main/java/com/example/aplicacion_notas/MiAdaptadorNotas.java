package com.example.aplicacion_notas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

    //Adaptador personalizado de Notas.
    public class MiAdaptadorNotas extends ArrayAdapter<Notas> {

        Context contexto;
        int recurso;

        public MiAdaptadorNotas(Context context,int resource, ArrayList<Notas> notitas){
            super(context, R.layout.list_item_notas , notitas);
            contexto = context;
            recurso = resource;
    }
        public View getView(int position, View convertView, ViewGroup parent){

            ViewHolder holder;
            /* Funciona de manera que solo se "infla" una vez la view para así no tener que acceder al archivo R múltiples veces
                cada vez que llamemos al método.
            */
            if (convertView == null){

                LayoutInflater inflater = LayoutInflater.from(contexto);
                //Cuidado, si marcamos true, se nuestros elementos se crearan del mismo modo que nuestro elemento padre, es decir lv
                convertView = inflater.inflate(recurso,parent,false);

                holder = new ViewHolder();
                holder.Titulo = (TextView) convertView.findViewById(R.id.tv_titulo);
                holder.Contenido = (TextView) convertView.findViewById(R.id.tv_contenido);
                holder.Fecha = (TextView) convertView.findViewById(R.id.tv_fecha);
                holder.Categoria = (TextView) convertView.findViewById(R.id.tv_categoria);

                /*El método setTag lo que hace es "dar memoria" a la view, vincula esa view con nuestro Holder creado previamente
                de manera que para acceder después a este, en vez de tener que volver a buscar en el archivo R cada elemento
                estos ya se encuentran guardados en el Holder, ya que no cambian*/
                convertView.setTag(holder);

            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            // Cuidado, es int , no es necesario enseñarlo
            //Id.setText((notas_arraylist.get(position).getId()) + "");
            //Id.setText(notas_arraylist.get(position).getId());
            holder.Titulo.setText(getItem(position).getTitulo());
            holder.Contenido.setText(getItem(position).getContenido());
            holder.Fecha.setText(getItem(position).getFechacreacion());
            holder.Categoria.setText(getItem(position).getCategoria());

            return convertView;
        }

        private static class ViewHolder {
            TextView Titulo;
            TextView Contenido;
            TextView Fecha;
            TextView Categoria;
        }

    }