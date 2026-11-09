package br.edu.ifal.cavalos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.edu.ifal.cavalos.ui.screen.RacasScreen
import br.edu.ifal.cavalos.ui.screen.TreinosScreen
import br.edu.ifal.cavalos.ui.theme.CavalosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // setContent inicia a interface declarativa: as funções descrevem o que exibir.
        setContent {
            CavalosTheme {
                CavalosApp()
            }
        }
    }
}

private enum class Destino(
    val rota: String,
    val titulo: String,
    val rotulo: String,
    val icone: ImageVector
) {
    Racas("racas", "Enciclopédia de Raças", "Raças", Icons.Default.Info),
    Treinos("treinos", "Rotina de Treinos", "Treinos", Icons.Default.DateRange)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CavalosApp() {
    // remember mantém o controlador entre recomposições (novas execuções da interface).
    val navController = rememberNavController()
    val entrada by navController.currentBackStackEntryAsState()
    val atual = Destino.entries.firstOrNull { it.rota == entrada?.destination?.route }
        ?: Destino.Racas

    // Scaffold organiza as barras e fornece o espaço que o conteúdo precisa respeitar.
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = { TopAppBar(title = { Text(atual.titulo) }) },
        bottomBar = {
            NavigationBar {
                Destino.entries.forEach { destino ->
                    NavigationBarItem(
                        selected = atual == destino,
                        onClick = {
                            if (atual != destino) {
                                navController.navigate(destino.rota) {
                                    // Guarda o estado da aba e evita empilhar telas repetidas.
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = { Icon(destino.icone, contentDescription = null) },
                        label = { Text(destino.rotulo) }
                    )
                }
            }
        }
    ) { padding ->
        // Cada rota associa um endereço de navegação a uma função de tela.
        NavHost(
            navController = navController,
            startDestination = Destino.Racas.rota,
            modifier = Modifier.padding(padding)
        ) {
            composable(Destino.Racas.rota) { RacasScreen() }
            composable(Destino.Treinos.rota) { TreinosScreen() }
        }
    }
}
