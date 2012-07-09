package pl.project13.kanbanery.activity

import pl.project13.scala.android.activity.{ContentView, ImplicitContext, ScalaActivity}
import pl.project13.scala.android.toast.ScalaToasts
import android.os.{Bundle, Handler}
import pl.project13.kanbanery.{R, TR}
import pl.project13.scala.android.util.ViewListenerConversions
import pl.project13.kanbanery.util.{KanbaneryPreferences, Intents}
import android.content.Context
import android.view.{View, LayoutInflater}
import android.widget.{ExpandableListView, SimpleExpandableListAdapter}
import pl.project13.janbanery.resources.{Project, Workspace}
import pl.project13.janbanery.JanbaneryFromSharedProperties
import pl.project13.scala.android.annotation.{MonsterDueToJavaApiIntegration, AssuresUiThread}
import collection.mutable.ListBuffer
import collection.JavaConversions._
import collection.JavaConverters._

class ProjectSelectionActivity extends ScalaActivity
with ImplicitContext with ScalaToasts
with ViewListenerConversions
with ContentView {

  implicit val handler = new Handler
  lazy val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE).asInstanceOf[LayoutInflater]

  lazy val WorkspacesView = {
    val view = findView(TR.workspaces)
    val emptyView = inflater.inflate(R.layout.empty_workspaces_create_one, null)
    view.setEmptyView(emptyView)
    view
  }

  val ContentView = TR.layout.workspaces_and_projects

  override def onCreate(bundle: Bundle) {
    super.onCreate(bundle)
  }

  override def onResume() {
    super.onResume()

    val login = getIntent.getExtras.getString(Intents.ProjectSelectionActivity.ExtraLogin)
    val pass = getIntent.getExtras.getString(Intents.ProjectSelectionActivity.ExtraPass)
    KanbaneryPreferences.login = login

    inFutureWithProgressDialog {
      val janbanery = JanbaneryFromSharedProperties.getUsingLoginAndPass(login, pass)
      KanbaneryPreferences.apiKey = janbanery.getAuthMode.getAuthHeader.getValue

      val workspaces = janbanery.workspaces.all()
      fillWorkspacesAndProjects(workspaces.toList)
    }
  }

  @AssuresUiThread
  def fillWorkspacesAndProjects(workspaces: List[Workspace]) = inUiThread {
    val adapter = new SimpleExpandableListAdapter(
      this,

      mapWorkspaces(workspaces),
      R.layout.workspaces_workspace,
      Array("name"),
      Array(R.id.workspace_name),

      projectsInWorkspace(workspaces),
      R.layout.workspaces_project,
      Array("name"),
      Array(R.id.project_name)
    )

    WorkspacesView setAdapter adapter
    WorkspacesView setOnChildClickListener new ExpandableListView.OnChildClickListener {
      override def onChildClick(expandableListView: ExpandableListView, view: View, groupId: Int, childId: Int, l: Long) = {
        val workspace = workspaces.get(groupId)
        val project = workspace.getProjects.get(childId)

        openBoardFor(workspace, project)
        true
      }
    }

    expandAllGroups(workspaces)
  }

  @MonsterDueToJavaApiIntegration
  def mapWorkspaces(workspaces: List[Workspace]) = {
    val result = ListBuffer[java.util.Map[String, Any]]()

    for (workspace <- workspaces) {
      val m = collection.mutable.Map[String, Any]()
      val workspaceName = workspace.getName

      m.put("name", workspaceName)
      info("Mapping workspace name: " + workspaceName)

      result += m.toMap.asJava
    }

    result.toList.asJava
  }

  @MonsterDueToJavaApiIntegration
  def projectsInWorkspace(workspaces: List[Workspace]): java.util.List[java.util.List[java.util.Map[String, Any]]] = {
    val result = ListBuffer[java.util.List[java.util.Map[String, Any]]]()

    for (workspace <- workspaces) {
      val secList = ListBuffer[java.util.Map[String, Any]]()

      val projects = workspace.getProjects
      for (project <- projects) {
        val projectData = collection.mutable.Map[String, Any]()
        val projectName = project.getName

        projectData.put("name", projectName)
        info("Mapping project name: " + projectName)

        secList += projectData.toMap.asJava
      }
      result += secList.toList.asJava
    }

    result.toList.asJava
  }

  def openBoardFor(workspace: Workspace, project: Project) {
    KanbaneryPreferences.workspaceName = workspace.getName
    KanbaneryPreferences.projectName = project.getName

    inUiThread {
      (KanbaneryPreferences.workspaceName, KanbaneryPreferences.projectName) match {
        case (Some(workspaceName), Some(projectName)) =>
          Intents.BoardActivity.start(KanbaneryPreferences.apiKey, workspaceName, projectName)
          finish()
        case e => ("No project was selected!" + e).toastLong
      }
    }
  }

  def expandAllGroups(workspaces: List[Workspace]) {
    (0 until workspaces.size) foreach {
      WorkspacesView.expandGroup
    }
  }


}