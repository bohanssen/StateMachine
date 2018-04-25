/**
 *
 */
package me.d2o.service.gameplay;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.d2o.persistence.model.game.DivisionTransfer;
import me.d2o.persistence.model.game.Territory;
import me.d2o.persistence.repository.DivisionTransferRepository;
import me.d2o.service.general.UserService;
import me.d2o.statemachine.GameEvent;
import me.d2o.transfermodel.gameplay.DivisionClientModel;
import me.d2o.utils.i18n.Translate;

/**
 * Class: DivisionTransferService
 *
 * @author bo.hanssen
 * @since Feb 4, 2017 11:55:04 PM
 *
 */
@Service
@Transactional
public class DivisionTransferService {

	@Autowired
	private DivisionTransferRepository divisionTransferRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private Translate translate;

	@Autowired
	private GameService gameService;

	public List<DivisionTransfer> getTransfers(GameEvent event) {
		return divisionTransferRepository.findDivisionTransferByGame(gameService.getGame(event));
	}

	public DivisionTransfer getTransfer(Territory from, Territory to, String unitKey) {
		DivisionTransfer transfer = divisionTransferRepository.findDivisionTransferBySuplierAndConsumerAndUnitKey(from,
				to, unitKey);
		if (transfer == null) {
			transfer = new DivisionTransfer();
			transfer.setConsumer(to);
			transfer.setSuplier(from);
			transfer.setUnit(from.getDivisions().get(unitKey).getUnit());
		}
		return transfer;
	}

	public List<DivisionClientModel> getDivisionTransfers(Territory territory) {
		return territory.getTransfer().stream().filter(d -> d.getQuantity() != 0)
				.map(t -> new DivisionClientModel(t.getUnit().getKey(),
						translate.getMessage(territory.getGame().getScenario().getTitleKey() + "."
								+ territory.getRole().getKey() + "." + t.getUnit().getKey(), userService.getUser()),
						t.getQuantity(), 0, 0, 0, 0))
				.collect(Collectors.toList());
	}

	public int totalUnits(Territory territory) {
		return territory.getTransfer().stream().mapToInt(d -> d.getQuantity()).sum();
	}

	public void remove(DivisionTransfer transfer) {
		transfer.getConsumer().getTransfer().remove(transfer);
		divisionTransferRepository.delete(transfer);
	}
}
