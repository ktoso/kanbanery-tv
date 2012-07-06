package pl.project13.android.scala.thread

import android.os.AsyncTask

abstract class VoidAsyncTask[Result] extends AsyncTask[AnyRef, AnyRef, Result]
