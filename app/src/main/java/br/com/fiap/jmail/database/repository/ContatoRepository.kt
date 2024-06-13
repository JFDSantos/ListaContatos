package br.com.fiap.jmail.database.repository

import android.content.Context
import br.com.fiap.jmail.database.dao.ContatoDb
import br.com.fiap.jmail.model.Contato

class ContatoRepository(context : Context) {

    var db = ContatoDb.getDatabase(context).contatoDao()

    fun salvar(contato: Contato) : Long{
        return db.salvar(contato)
    }

    fun atualizar(contato: Contato) : Int{
        return db.atualizar(contato)
    }

    fun excluir(contato: Contato) : Int{
        return db.excluir(contato)
    }

    fun buscarContatoId(id : Int) : Contato{
        return db.buscarContatoPeloId(id)
    }

    fun buscarTodos() : List<Contato>{
        return db.listarContatos()
    }
}