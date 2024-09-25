import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AuthManager {
    fun login(username: String, password: String): Boolean {
        // Simple authentication logic
        return username == "user" && password == "password"
    }
}


@Composable
fun LoginScreen(navController: NavController, authManager: AuthManager = AuthManager()) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFE91E63))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 100.dp)
                .align(Alignment.Center)
        ) {
            Text("Welcome!",
                color = Color.White,
                fontSize = 46.sp,
                fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(32.dp))

            TextField(
                value = username,
                onValueChange = { username = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Username") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (authManager.login(username, password)) {
                        navController.navigate("calendar_screen_route")
                    } else {
                        // Handle login failure
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Login")
            }
        }
    }
}

@Composable
fun CalendarScreen() {
    var currentDate by remember { mutableStateOf(Calendar.getInstance()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        WeekNavigation(
            currentDate = currentDate,
            onPreviousWeek = {
                currentDate = (currentDate.clone() as Calendar).apply {
                    add(Calendar.WEEK_OF_YEAR, -1)
                }
            },
            onNextWeek = {
                currentDate = (currentDate.clone() as Calendar).apply {
                    add(Calendar.WEEK_OF_YEAR, 1)
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        WeekInfo(currentDate)
        Spacer(modifier = Modifier.height(8.dp))
        WeekView(currentDate)
    }
}

@Composable
fun WeekNavigation(
    currentDate: Calendar,
    onPreviousWeek: () -> Unit,
    onNextWeek: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Previous Week",
            modifier = Modifier.clickable { onPreviousWeek() }
        )
        Text(
            text = "Week ${getMonthName(currentDate.get(Calendar.MONTH))}",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = "Next Week",
            modifier = Modifier.clickable { onNextWeek() }
        )
    }
}

@Composable
fun WeekInfo(currentDate: Calendar) {
    val weekOfMonth = currentDate.get(Calendar.WEEK_OF_MONTH)
    Text(
        text = "Week $weekOfMonth of the month",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun WeekView(currentDate: Calendar) {
    val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    val dateFormat = SimpleDateFormat("dd", Locale.getDefault())

    // Calculate the start of the week
    val startOfWeek = currentDate.clone() as Calendar
    startOfWeek.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            for (i in 0 until 7) {
                val day = startOfWeek.clone() as Calendar
                day.add(Calendar.DAY_OF_WEEK, i)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                        .background(Color.LightGray, RoundedCornerShape(4.dp))
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = dateFormat.format(day.time),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

fun getMonthName(month: Int): String {
    val monthNames = arrayOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )
    return monthNames[month]
}

@Preview
@Composable
fun PreviewCalendarScreen() {
    CalendarScreen()
}