package nl.hypothermic.ofts.game;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SplittableRandom;

public class EntityTracker {
	
	// ==== EntityId allocation ==== //
	// 0-63 = reserved space
	// 64-4095 = current EID field
	// ==== BlockId allocation ===== //
	// 0-4095 = allowed by client
	// ============================= //
	
	public static final EntityTracker instance = new EntityTracker();
	
	private volatile HashMap<Integer, Entity> map = new HashMap<Integer, Entity>();
	
	private SplittableRandom random = new SplittableRandom();
	
	public int registerEntity(Entity entity) {
		int newId = random.nextInt(63, 4095);
		while (newId < 64 || map.keySet().contains(newId)) {
			newId = random.nextInt(63, 4095);
		}
		map.put(newId, entity);
		return newId;
	}
	
	public void unregisterEntity(int entityId) {
		map.remove(entityId);
	}
	
	public void unregisterEntity(Entity entity) {
		Set<Entry<Integer, Entity>> entrySet = map.entrySet();
        for (Iterator iterator = entrySet.iterator(); iterator.hasNext();) {
            Entry<Integer, Entity> entry = (Entry<Integer, Entity>) iterator.next();
            if (entry.getValue().equals(entity)) {
            	iterator.remove();
            }
        }
	}
}
