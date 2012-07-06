package pl.project13.scala.android.thread

import android.os.AsyncTask

abstract class VoidAsyncTask[Result] extends AsyncTask[AnyRef, AnyRef, Result]
