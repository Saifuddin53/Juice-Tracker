/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.juicetracker.ui

import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.juicetracker.data.Juice
import com.example.juicetracker.data.JuiceColor
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.juicetracker.R

class JuiceListAdapter(
    private var onEdit: (Juice) -> Unit,
    private var onDelete: (Juice) -> Unit
) : ListAdapter<Juice, JuiceListAdapter.JuiceListViewHolder>(JuiceDiffCallback()) {

    class JuiceListViewHolder(
        private val onEdit: (Juice) -> Unit,
        private val onDelete: (Juice) -> Unit
    ) : RecyclerView.ViewHolder(composeView) {

        fun bind(juice: Juice) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JuiceListViewHolder {
        return JuiceListViewHolder(
            ComposeView(parent.context),
            onEdit,
            onDelete
        )
    }

    override fun onBindViewHolder(holder: JuiceListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class JuiceDiffCallback : DiffUtil.ItemCallback<Juice>() {
    override fun areItemsTheSame(oldItem: Juice, newItem: Juice): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Juice, newItem: Juice): Boolean {
        return oldItem == newItem
    }
}


@Composable
fun ListItem(
    input: Juice,
    onDelete: (Juice) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(2.dp, Color.Black)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(16.dp)
        ) {
            Row {
                Row {
                    Icon(painter = painterResource(id = R.drawable.ic_juice_color),
                        contentDescription = "")
                    Column {
                        Text(text = input.name,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 24.sp)
                        Text(text = input.description)
                    }
                }
                //element
            }
        }
    }
}

@Composable
fun JuiceIcon(color: String, modifier: Modifier = Modifier) {
    val colorLabelMap = JuiceColor.values().associateBy { stringResource(it.label) }
    val selectedColor = colorLabelMap[color]?.let { Color(it.color) }
    val juiceIconContentDescription = stringResource(R.string.juice_color, color)

    Box(
        modifier.semantics {
            contentDescription = juiceIconContentDescription
        }
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_juice_color),
            contentDescription = null,
            tint = selectedColor ?: Color.Red,
            modifier = Modifier.align(Alignment.Center)
        )
        Icon(painter = painterResource(R.drawable.ic_juice_clear), contentDescription = null)
    }
}

@Composable
fun JuiceDetails(juice: Juice, modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.Top) {
        Text(
            text = juice.name,
            style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
        )
        Text(juice.description)
        RatingDisplay(rating = juice.rating, modifier = Modifier.padding(top = 8.dp))
    }
}

@Composable
fun RatingDisplay(rating: Int, modifier: Modifier = Modifier) {
    val displayDescription = pluralStringResource(R.plurals.number_of_stars, count = rating)
    Row(
        // Content description is added here to support accessibility
        modifier.semantics {
            contentDescription = displayDescription
        }
    ) {
        repeat(rating) {
            // Star [contentDescription] is null as the image is for illustrative purpose
            Image(
                modifier = Modifier.size(32.dp),
                painter = painterResource(R.drawable.star),
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
fun ListItemPreview() {
    ListItem(input = Juice(0L, "", "", "", 4), onDelete = {})
}