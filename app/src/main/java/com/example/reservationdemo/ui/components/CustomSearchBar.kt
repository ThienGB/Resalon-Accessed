package com.example.reservationdemo.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.reservationdemo.R

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun CustomSearchBar(
    modifier: Modifier = Modifier,
    text: String = "",
    onTextChange: (String) -> Unit = {},
    leadingIcon: Int = R.drawable.ic_search,
    placeholder: String = "Tìm kiếm theo tên hoặc email",
    isDisable: Boolean = false,
){
    val interactionSource = remember { MutableInteractionSource() }
    var message by remember {
        mutableStateOf(text)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .clip(RoundedCornerShape(9.dp)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicTextField(
            value = text,
            onValueChange = { newValue ->
                message = newValue
                onTextChange(newValue)
            },
            textStyle = TextStyle(
                fontSize = 15.sp,
                color = LocalContentColor.current // đảm bảo giữ màu text đúng
            ),
            enabled = !isDisable,
            singleLine = true,
            modifier = modifier
                .height(46.dp)
                .weight(1f)
                .clip(RoundedCornerShape(9.dp))
                .border(
                    width = 1.dp,
                    color = colorResource(id = R.color.colorSeparator),
                    shape = RoundedCornerShape(9.dp)
                ),
            decorationBox = { innerTextField ->
                TextFieldDefaults.DecorationBox(
                    value = message,
                    innerTextField = innerTextField,
                    enabled = true,
                    singleLine = true,
                    visualTransformation = VisualTransformation.None,
                    interactionSource = interactionSource,
                    placeholder = {
                        if ("•" in placeholder) {
                            val parts = placeholder.split("•", limit = 2).map { it.trim() }
                            Text(
                                buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight(600), color = Color.Gray)) {
                                        append(parts[0])
                                    }
                                    append(" • ")
                                    withStyle(style = SpanStyle(color = Color.LightGray)) {
                                        append(parts.getOrNull(1) ?: "")
                                    }
                                },
                                fontSize = 15.sp
                            )
                        } else {
                            Text(text = placeholder, fontSize = 15.sp, color = Color.Gray)
                        }
                    },
                    leadingIcon = {
                        Image(
                            painter = painterResource(leadingIcon),
                            contentDescription = null,
                            modifier = Modifier.size(25.dp)
                        )
                    },
                    trailingIcon = {
                        if (message.isNotEmpty()) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_cancel),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(2.dp)
                                    .clickable {
                                        message = ""
                                        onTextChange("")
                                    }
                            )
                        }
                    },
                    contentPadding = PaddingValues(),
                    container = {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = Color.Transparent,
                                    shape = RoundedCornerShape(9.dp)
                                )
                        )
                    },
                )
            }
        )
    }

}