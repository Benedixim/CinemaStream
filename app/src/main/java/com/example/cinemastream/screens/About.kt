package com.example.cinemastream.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cinemastream.R
import org.jetbrains.annotations.ApiStatus


@ApiStatus.Internal
@Composable
fun about() {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .background(color = Color(0xFF542962))
            .padding(top = 70.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .paddingFromBaseline(top = 48.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile_photo),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(width = 139.dp, height = 141.dp)
                        .clip(shape = CircleShape)

                )

                Text(
                    text = "Андрей Колесинский\nJava Developer",
                    color = Color(0xFFF1A4CA),
                    fontSize = 18.sp,
                    fontFamily = FontFamily.SansSerif,
                    letterSpacing = 0.02.sp,
                    lineHeight = 26.sp,
                    modifier = Modifier
                        .padding(start = 16.dp, top = 25.dp, end = 16.dp, bottom = 8.dp)
                        .shadow(
                            elevation = 4.dp,
                            shape = RoundedCornerShape(4.dp),
                            clip = true
                        )

                        .clip(shape = RoundedCornerShape(4.dp))

                )
            }
            val context = LocalContext.current




            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .paddingFromBaseline(top = 48.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.faq),
                    contentDescription = null,
                    modifier = Modifier
                        .width(70.dp)
                        .height(50.dp)
                        .padding(start = 30.dp)
                )
                Column(
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = "FAQ",
                        color = Color(0xFFF1A4CA),
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Need more help?",
                        color = Color(0xFFC569A0),
                        fontSize = 10.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .paddingFromBaseline(top = 38.dp)

            ) {
                Image(
                    painter = painterResource(id = R.drawable.github),
                    contentDescription = null,
                    modifier = Modifier
                        .width(70.dp)
                        .height(50.dp)
                        .padding(start = 30.dp)
                )
                Column(
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = "Github",
                        color = Color(0xFFF1A4CA),
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Fork the project on Github",
                        color = Color(0xFFC569A0),
                        fontSize = 10.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .paddingFromBaseline(top = 38.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.linkedin),
                    contentDescription = null,
                    modifier = Modifier
                        .width(70.dp)
                        .height(50.dp)
                        .padding(start = 30.dp)
                )
                Column(
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = "Linkedin",
                        color = Color(0xFFF1A4CA),
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Contact us on Linkedin",
                        color = Color(0xFFC569A0),
                        fontSize = 10.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .paddingFromBaseline(top = 38.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.vk),
                    contentDescription = null,
                    modifier = Modifier
                        .width(70.dp)
                        .height(50.dp)
                        .padding(start = 30.dp)
                )
                Column(
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = "VKontacte",
                        color = Color(0xFFF1A4CA),
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Visit our VKontakte page",
                        color = Color(0xFFC569A0),
                        fontSize = 10.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .paddingFromBaseline(top = 38.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.email),
                    contentDescription = null,
                    modifier = Modifier
                        .width(70.dp)
                        .height(50.dp)
                        .padding(start = 30.dp)
                )
                Column(
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = "Email",
                        color = Color(0xFFF1A4CA),
                        fontSize = 18.sp
                    )
                    Text(
                        text = "benediximus21@mail.ru",
                        color = Color(0xFFC569A0),
                        fontSize = 10.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .paddingFromBaseline(top = 38.dp, bottom = 80.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.question),
                    contentDescription = null,
                    modifier = Modifier
                        .width(70.dp)
                        .height(50.dp)
                        .padding(start = 30.dp)
                )
                Column(
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = "Any other questions?",
                        color = Color(0xFFF1A4CA),
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Ask them here",
                        color = Color(0xFFC569A0),
                        fontSize = 10.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }



            // Add your code for the list here

        }
    }
}

