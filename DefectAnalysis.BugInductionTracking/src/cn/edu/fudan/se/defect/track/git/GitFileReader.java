/**
 * 
 */
package cn.edu.fudan.se.defect.track.git;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.WorkingTreeOptions;
import org.eclipse.jgit.util.io.AutoCRLFInputStream;

import cn.edu.fudan.se.defect.track.constants.BugTrackingConstants;

/**
 * @author Lotay
 * 
 */
public class GitFileReader {

	private Repository repo = null;
	private Git git = null;
	private TreeWalk treeWalk = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String revisionId = "9d4d144eeab514c396e252f897178d49b4ecb5c3", fileName = "org.eclipse.jdt.core/compiler/org/eclipse/jdt/internal/compiler/lookup/SourceTypeBinding.java";
		GitFileReader reader = new GitFileReader();
		System.out.println("main");
		String value = new String(reader.readGitFile(revisionId, fileName));
		System.out.println(value);
	}

	{
		try {
			repo = new FileRepository(new File(
					BugTrackingConstants.ECLIPSE_CORE_GIT_REPO_PATH));
			git = new Git(repo);
			treeWalk = new TreeWalk(repo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte[] readGitFile(String revisionId, String fileName) {
		if (revisionId == null || fileName == null) {
			System.err.println("revisionId/fileName is null..");
			return null;
		}

		if (repo == null || git == null || treeWalk == null) {
			System.err.println("git repo is null..");
			return null;
		}
		RevWalk walk = new RevWalk(repo);

		try {
			ObjectId objId = repo.resolve(revisionId);
			if (objId == null) {
				System.err.println("The revision:" + revisionId
						+ " does not exist.");
				return null;
			}
			RevCommit commit = walk.parseCommit(repo.resolve(revisionId));
			if (commit != null) {
				RevTree tree = commit.getTree();
				TreeWalk treeWalk = TreeWalk.forPath(repo, fileName, tree);
				ObjectId id = treeWalk.getObjectId(0);
				InputStream is = open(id, repo);
				byte[] byteArray = IOUtils.toByteArray(is);
				return byteArray;
			} else {
				System.err.println("Cannot found file(" + fileName
						+ ") in revision(" + revisionId + "): "
						+ treeWalk.getPathString());
			}
		} catch (RevisionSyntaxException e) {
			e.printStackTrace();
		} catch (MissingObjectException e) {
			e.printStackTrace();
		} catch (IncorrectObjectTypeException e) {
			e.printStackTrace();
		} catch (AmbiguousObjectException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private static InputStream open(ObjectId blobId, Repository db)
			throws IOException, IncorrectObjectTypeException {
		if (blobId == null)
			return new ByteArrayInputStream(new byte[0]);

		try {
			WorkingTreeOptions workingTreeOptions = db.getConfig().get(
					WorkingTreeOptions.KEY);
			switch (workingTreeOptions.getAutoCRLF()) {
			case INPUT:
				// When autocrlf == input the working tree could be either CRLF
				// or LF, i.e. the comparison
				// itself should ignore line endings.
			case FALSE:
				return db.open(blobId, Constants.OBJ_BLOB).openStream();
			case TRUE:
			default:
				return new AutoCRLFInputStream(db.open(blobId,
						Constants.OBJ_BLOB).openStream(), true);
			}
		} catch (MissingObjectException notFound) {
			return null;
		}
	}
}
