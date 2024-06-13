package br.com.fiap.jmail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.jmail.database.repository.ContatoRepository
import br.com.fiap.jmail.model.Contato
import br.com.fiap.jmail.ui.theme.JMailTheme

data class Email(val sender: String, val subject: String, val body: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JMailTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        ContatosScreen()
                    }
                }
            }
        }
    }
}


@Composable
fun ContatosScreen() {

    var nomeState = remember {
        mutableStateOf("")
    }

    var telefoneState = remember {
        mutableStateOf("")
    }

    var amigoState = remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current
    val contatoRepository = ContatoRepository(context)

    var listaContatoState = remember {
        mutableStateOf(contatoRepository.buscarTodos())
    }

    Column {
        ContatoForm(
            nome = nomeState.value,
            telefone = telefoneState.value,
            amigo = amigoState.value,
            onNomeChange = {
                nomeState.value = it
            },
            onTelefoneChange = {
                telefoneState.value = it
            },
            onAmigoChange = {
                amigoState.value = it
            },
            atualizar = {
                listaContatoState.value = contatoRepository.buscarTodos()
            }
        )
        ContatoList(listaContatoState, atualizar = {listaContatoState.value = contatoRepository.buscarTodos()})
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContatoForm(
    nome: String,
    telefone: String,
    amigo: Boolean,
    onNomeChange: (String) -> Unit,
    onTelefoneChange: (String) -> Unit,
    onAmigoChange: (Boolean) -> Unit,
    atualizar: () -> Unit
) {
    val context = LocalContext.current
    val contatoRepository = ContatoRepository(context)

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Cadastro de contatos",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(
                0xFFE91E63
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = nome,
            onValueChange = { onNomeChange(it) },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Nome do contato")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = telefone,
            onValueChange = { onTelefoneChange(it) },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Telefone do contato")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(checked = amigo, onCheckedChange = {
                onAmigoChange(it)
            })
            Text(text = "Amigo")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                val contato = Contato(0, nome, telefone, amigo)
                contatoRepository.salvar(contato)
                atualizar()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "CADASTAR",
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun ContatoList(listaContatos: MutableState<List<Contato>>, atualizar: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        for (contatos in listaContatos.value) {
            ContatoCard(contatos, atualizar)
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun ContatoCard(contatos: Contato, atualizar: () -> Unit) {

    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.LightGray
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(2f)
            ) {
                Text(
                    text = contatos.nome,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = contatos.telefone,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (contatos.amigo) "Amigo" else "Contato",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            IconButton(onClick = {
                val contatoRepository = ContatoRepository(context)
                contatoRepository.excluir(contatos)
                atualizar()
            }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = ""
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewEmailInbox() {
    ContatosScreen()
}