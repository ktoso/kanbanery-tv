package pl.project13.janbanery

import _root_.android.content.Context
import android.rest.AndroidCompatibleRestClient
import core.{JanbaneryFactory, Janbanery}
import pl.project13.kanbanery.util.KanbaneryPreferences
import collection.JavaConversions._
import resources.Workspace

object JanbaneryFromSharedProperties {

  def newRestClient = new AndroidCompatibleRestClient

  def getUsingApiKey()(implicit ctx: Context): Janbanery = KanbaneryPreferences.apiKey match {
    case Some(apiKey) =>
    val using = new JanbaneryFactory(newRestClient)
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
    new JanbaneryFactory(newRestClient)
      .connectUsing(login, pass)
      .notDeclaringWorkspaceYet()
  }
}
