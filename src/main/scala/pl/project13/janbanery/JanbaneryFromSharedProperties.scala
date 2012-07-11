package pl.project13.janbanery

import _root_.android.content.Context
import android.rest.AndroidCompatibleRestClient
import core.{JanbaneryFactory, Janbanery}
import pl.project13.kanbanery.util.KanbaneryPreferences

object JanbaneryFromSharedProperties {

  val RestClient = new AndroidCompatibleRestClient

  def getUsingApiKey()(implicit ctx: Context): Janbanery = KanbaneryPreferences.apiKey match {
    case Some(apiKey) =>
    val using = new JanbaneryFactory(RestClient)
      .connectUsing(apiKey)

    val janbaneryOnWorkspace = KanbaneryPreferences.workspaceName match {
      case Some(workspaceName) => using.toWorkspace(workspaceName)
      case _ => using.notDeclaringWorkspaceYet()
    }

    KanbaneryPreferences.projectName match {
      case Some(projectName) => janbaneryOnWorkspace.usingProject(projectName)
      case _ => janbaneryOnWorkspace
    }
  }

  def getUsingLoginAndPass(login: String, pass: String)(implicit ctx: Context): Janbanery = {
    new JanbaneryFactory(RestClient)
      .connectUsing(login, pass)
      .notDeclaringWorkspaceYet()
  }
}
