package com.example.livefrontcodechallenge.view;

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.decode.VideoFrameDecoder
import com.example.livefrontcodechallenge.R
import com.example.livefrontcodechallenge.data.ApodMediaType
import com.example.livefrontcodechallenge.data.ApodModel
import java.time.format.DateTimeFormatter

private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ISO_DATE

@Composable
fun ApodCardView(model: ApodModel) {
  Card(
    elevation = 4.dp, modifier = Modifier
      .padding(6.dp, 4.dp)
      .fillMaxWidth()
  ) {
    Box {
      Column {
        ApodThumbnailView(model)
        Column(
          modifier = Modifier.padding(4.dp)
        ) {
          CardViewTextLabel(
            stringResource(id = R.string.label_title, model.title)
          )
          CardViewTextLabel(
            stringResource(id = R.string.label_date, dateFormatter.format(model.date))
          )
        }
      }
    }
  }
}

@Composable
private fun ApodThumbnailView(model: ApodModel) {
  when (model.mediaType) {
    ApodMediaType.IMAGE -> ApodImageThumbnailView(model)
    ApodMediaType.VIDEO -> ApodVideoThumbnailView(model)
    ApodMediaType.UNKNOWN -> {} // no-op, don't display anything
  }
}

@Composable
private fun ApodImageThumbnailView(model: ApodModel) {
  // todo: add placeholder image
  AsyncImage(
    modifier = Modifier
      .height(100.dp)
      .fillMaxWidth(),
    model = (model.thumbnailUrl ?: model.url),
    contentDescription = model.title,
    contentScale = ContentScale.FillWidth
    // placeholder = painterResource(id = R.drawable.placeholder)
  )
}

@Composable
private fun ApodVideoThumbnailView(model: ApodModel) {
  // todo: couldn't get this to display very well (or at all in many cases) possibly due to the API
  // returning different video hosts, e.g. vimeo or youtube, so it's a stretch goal
  val imageLoader =
    ImageLoader.Builder(LocalContext.current).components { add(VideoFrameDecoder.Factory()) }
      .build()
  val painter = rememberAsyncImagePainter(model = model.url, imageLoader = imageLoader)
  Image(
    painter = painter,
    contentDescription = model.title,
  )
}

@Composable
private fun CardViewTextLabel(strValue: String) {
  Text(
    text = strValue,
    maxLines = 1,
    overflow = TextOverflow.Ellipsis,
    modifier = Modifier.padding(2.dp)
  )
}