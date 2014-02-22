package custom_java_utils;

public class ProjectPathUtils {
	private static String workspaceDirectory;
	
	private static void initialize() {
	       workspaceDirectory = System.getProperty("user.dir");
	       int projectFolderPosition = workspaceDirectory.lastIndexOf("/team_project/go_problem_solver");
	       int projectFolderPositionWindows = workspaceDirectory.lastIndexOf("\\team_project\\go_problem_solver");
	       boolean isWindows = workspaceDirectory.indexOf(":\\") > 0;
	       if (Math.max(projectFolderPosition, projectFolderPositionWindows) < 0) {
	    	   if (isWindows) {
	    		   workspaceDirectory += "\\team_project\\go_problem_solver";
	    	   } else {
	    		   workspaceDirectory += "/team_project/go_problem_solver";
	    	   }
	       }
	}
	
	public static String getWorkspaceDir() {
		if (workspaceDirectory == null) {
			initialize();
		}
		return workspaceDirectory;
	}

}
