package keen.shared;


import javax.persistence.Id;

import com.googlecode.objectify.annotation.*;
import com.googlecode.objectify.condition.*;

@Entity
public class Media {
	@Id public Long id;
	public String owner;
	@Unindexed(IfFalse.class) public boolean toDelete;

	protected Media() {}
}
