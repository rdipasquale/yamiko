package ar.edu.ungs.yamiko.problems.vrp.utils.hdfs;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;

public class CopyGraphToLocal {

	private static Configuration conf;
	private static FileSystem fs ;
	
	public static void copy(final String uri, final String localTmp) throws IOException 
	{
		if (conf==null) conf = new Configuration();
		try {
			if (fs==null) fs = FileSystem.get(URI.create(uri), conf);
			Logger.getLogger(CopyGraphToLocal.class).warn("Copiando " + uri + " => " + localTmp + " : " + InetAddress.getLocalHost().getHostName() );
			Path hdfs = new Path(uri);
			Path local = new Path(localTmp);
			if (new File(localTmp).exists())
			{
				if (new File(localTmp).lastModified()<fs.getFileStatus(hdfs).getModificationTime())
					if (fs.exists(hdfs))
						fs.copyToLocalFile(false, hdfs , local, true);
			}
			else
				if (fs.exists(hdfs))
					fs.copyToLocalFile(false, hdfs , local, true);
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.getLogger(CopyGraphToLocal.class).error("Error Copiando " + uri + " => " + localTmp );
			throw e;
		}
	}
	
}
