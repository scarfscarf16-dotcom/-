package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Surah
import com.example.data.model.toArabicDigits
import com.example.data.repository.QuranRepository
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGoldLight
import com.example.ui.theme.IslamicGreenMedium
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder
import com.example.ui.theme.SurfaceDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurahAyahPickerSheet(
    selectedSurah: Surah,
    fromAyah: Int,
    toAyah: Int,
    onSelectionConfirmed: (Surah, Int, Int) -> Unit,
    onAutoFitDuration: (Surah, Int) -> Unit,
    sheetState: SheetState,
    onDismissRequest: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var tempSurah by remember { mutableStateOf(selectedSurah) }
    var tempFromAyah by remember { mutableStateOf(fromAyah) }
    var tempToAyah by remember { mutableStateOf(toAyah) }

    val filteredSurahs = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            QuranRepository.ALL_SURAHS
        } else {
            QuranRepository.ALL_SURAHS.filter {
                it.nameArabic.contains(searchQuery.trim()) ||
                    it.nameEnglish.contains(searchQuery.trim(), ignoreCase = true) ||
                    it.id.toString() == searchQuery.trim()
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = SurfaceDark,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(IslamicGreenMedium),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Book,
                            contentDescription = null,
                            tint = IslamicGold,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "مكتبة القرآن الكريم",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = Color.White
                        )
                        Text(
                            text = "اختر السورة ونطاق الآيات للمقطع",
                            fontFamily = CairoFontFamily,
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.65f)
                        )
                    }
                }

                IconButton(onClick = onDismissRequest) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "إغلاق",
                        tint = Color.White.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(SurfaceCard)
                    .border(1.dp, SurfaceCardBorder, RoundedCornerShape(14.dp))
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "سورة ${tempSurah.nameArabic} (${tempSurah.versesCount} آية)",
                        fontFamily = CairoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = IslamicGoldLight
                    )

                    Button(
                        onClick = {
                            onAutoFitDuration(tempSurah, tempFromAyah)
                            onDismissRequest()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = IslamicGreenMedium,
                            contentColor = IslamicGoldLight
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "حساب تكميل 60ث+",
                            fontFamily = CairoFontFamily,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "من الآية",
                            fontFamily = CairoFontFamily,
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = {
                                    if (tempFromAyah > 1) {
                                        tempFromAyah--
                                        if (tempToAyah < tempFromAyah) tempToAyah = tempFromAyah
                                    }
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Default.Remove, null, tint = IslamicGold, modifier = Modifier.size(16.dp))
                            }
                            Text(
                                text = tempFromAyah.toArabicDigits(),
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            IconButton(
                                onClick = {
                                    if (tempFromAyah < tempSurah.versesCount) {
                                        tempFromAyah++
                                        if (tempToAyah < tempFromAyah) tempToAyah = tempFromAyah
                                    }
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Default.Add, null, tint = IslamicGold, modifier = Modifier.size(16.dp))
                            }
                        }
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "إلى الآية",
                            fontFamily = CairoFontFamily,
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = {
                                    if (tempToAyah > tempFromAyah) {
                                        tempToAyah--
                                    }
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Default.Remove, null, tint = IslamicGold, modifier = Modifier.size(16.dp))
                            }
                            Text(
                                text = tempToAyah.toArabicDigits(),
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            IconButton(
                                onClick = {
                                    if (tempToAyah < tempSurah.versesCount) {
                                        tempToAyah++
                                    }
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Default.Add, null, tint = IslamicGold, modifier = Modifier.size(16.dp))
                            }
                        }
                    }

                    Button(
                        onClick = {
                            onSelectionConfirmed(tempSurah, tempFromAyah, tempToAyah)
                            onDismissRequest()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = IslamicGold,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .testTag("confirm_ayah_range_button")
                            .height(40.dp)
                    ) {
                        Text(
                            text = "تطبيق",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("surah_search_input"),
                placeholder = {
                    Text(
                        text = "ابحث عن السورة بالاسم أو الرقم...",
                        fontFamily = CairoFontFamily,
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = IslamicGold
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = IslamicGold,
                    unfocusedBorderColor = SurfaceCardBorder,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredSurahs) { surah ->
                    val isSelected = tempSurah.id == surah.id
                    Box(
                        modifier = Modifier
                            .testTag("surah_item_${surah.id}")
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) SurfaceCardBorder else SurfaceCard)
                            .border(
                                1.dp,
                                if (isSelected) IslamicGold else SurfaceCardBorder,
                                RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                tempSurah = surah
                                tempFromAyah = 1
                                tempToAyah = surah.versesCount.coerceAtMost(5)
                            }
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) IslamicGold else IslamicGreenMedium),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = surah.id.toArabicDigits(),
                                        fontFamily = CairoFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = if (isSelected) Color.Black else IslamicGoldLight
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "سورة ${surah.nameArabic}",
                                        fontFamily = CairoFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "${surah.nameEnglish} • ${surah.versesCount} آيات • ${surah.revelationType.arabicLabel}",
                                        fontFamily = CairoFontFamily,
                                        fontSize = 11.sp,
                                        color = Color.White.copy(alpha = 0.6f)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
