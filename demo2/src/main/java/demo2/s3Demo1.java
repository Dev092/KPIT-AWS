package demo2;


import java.util.List;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

public class s3Demo1 {
	


	public static void main(String[] args) {
		S3Client client = S3Client.builder()
								.region(Region.US_EAST_1)
								.credentialsProvider(DefaultCredentialsProvider.create())
								.build();
		List<Bucket> buckets = client.listBuckets().buckets();
		System.out.println("Found "+ buckets.size() +" buckets ");
		for(Bucket b : buckets) {
			System.out.println("Bucket : "+b.name());
			ListObjectsRequest req = ListObjectsRequest.builder().bucket(b.name()).build();
			List<S3Object> objects = client.listObjects(req).contents();
			System.out.println("  Number of objects found: "+objects.size());
			
		}
		/**
		 * 	S3Client client = S3Client.builder().build();
		 * 
		 */
		

	}

}

