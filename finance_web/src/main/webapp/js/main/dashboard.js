function tvoDashboard() {
	this.init = function () {
		chart.refresh();

		// 현재 시각
		setInterval(() => {
			//$('.rightGrp button.btnUpdate').text(moment().format('A HH:mm:ss'));
			$('.rightGrp button.btnUpdate').text(moment().format('YYYY-MM-DD A HH:mm:ss'));
		}, 1000);

		$('.rightGrp button.btnUpdate').on('click', () => chart.refresh());
		$('.rightGrp a.btnPopClose').on('click', () => dashboard.close());
	}

	const dashboard = {
		close: function () {
			self.close();
		}
	}

	const chart = {
		timeout: null,
		apexCharts: {
			tvoViewAprvStateDaily: null,		// 금월 일자별 영상 열람승인 현황
			tvoOutAprvStateDaily: null,			// 금월 일자별 영상 반출승인 현황
			
			tvoViewAprvStateMonthly: null,		// 금월 영상 열람승인 현황
			tvoOutAprvStateMonthly: null,		// 금월 영상 반출승인 현황
		},
		refresh: function () {
			let fnRefresh = () => {
				$.ajax({
					type: 'POST',
					url:contextRoot + '/main/dashboardData.json',
					success: function (data) {

						if (data.tvoViewState != null) {		// 금일 열람 현황
							$('strong.tvoViewState.cntRqst').text(data.tvoViewState.cntRqst);
							$('strong.tvoViewState.cntPrgrs10').text(data.tvoViewState.cntPrgrs10);
						}
						
						if (data.tvoCctvState != null) {		// 금일 열람연장 현황
							$('strong.tvoViewExtnState.cntRqst').text(data.tvoViewExtnState.cntRqst);
							$('strong.tvoViewExtnState.cntPrgrs10').text(data.tvoViewExtnState.cntPrgrs10);
						}
						
						if (data.tvoCctvState != null) {		// 금일 반출 현황
							$('strong.tvoOutState.cntRqst').text(data.tvoOutState.cntRqst);
							$('strong.tvoOutState.cntPrgrs10').text(data.tvoOutState.cntPrgrs10);
						}
						
						if (data.tvoCctvState != null) {		// 금일 반출연장 현황
							$('strong.tvoOutExtnState.cntRqst').text(data.tvoOutExtnState.cntRqst);
							$('strong.tvoOutExtnState.cntPrgrs10').text(data.tvoOutExtnState.cntPrgrs10);
						}
											
						if (data.tvoCctvState != null) {		// CCTV 현황, CCTV 댓수
							$('strong.tvoCctvState.cntCctv').text(parseInt(data.tvoCctvState.cntCctv).toLocaleString());
							$('strong.tvoCctvState.cntIvaTy1').text(parseInt(data.tvoCctvState.cntIvaTy1).toLocaleString());
							$('strong.tvoCctvState.cntIvaTy2').text(parseInt(data.tvoCctvState.cntIvaTy2).toLocaleString());
						}
									
						if (data.storageState != null) {		// 스토리지 현황
							if ( data.storageState.totalSizeTb > 1 ) {
								$('strong.storageState.totalSize').text(String(data.storageState.totalSizeTb)+'TB');
								$('strong.storageState.freeSize').text(String(data.storageState.freeSizeTb)+'TB');
							} else {
								$('strong.storageState.totalSize').text(parseInt(data.storageState.totalSizeGb).toLocaleString()+'GB');
								$('strong.storageState.freeSize').text(parseInt(data.storageState.freeSizeGb).toLocaleString()+'GB');
							}
							$('.freeRate').text(data.storageState.freeRate+'%');
							//$('strong.storageState.usedSize').text(data.storageState.freeSize+'GB');
							//$('strong.storageState.usedRate').text(data.storageState.usedRate+'%');
						}
						
						chart.tvoViewAprvStateDaily(data.tvoViewAprvStateDaily);			// 금월 일자별 영상 열람승인 현황
						chart.tvoOutAprvStateDaily(data.tvoOutAprvStateDaily);				// 금월 일자별 영상 반출승인 현황
						
						chart.tvoViewAprvStateMonthly(data.tvoViewAprvStateMonthly);		// 금월 영상 열람승인 현황
						chart.tvoOutAprvStateMonthly(data.tvoOutAprvStateMonthly);			// 금월 영상 반출승인 현황
						
					},
					error: function (xhr, status, error) {
						console.log(xhr, status, error);
					},
				})
			};
			fnRefresh();

			if (chart.timeout != null) {
				clearInterval(chart.timeout);
				chart.timeout = null;
			}

			chart.timeout = setTimeout(() => {
				fnRefresh();
			}, 1000 * 60);
		},
		
		
		
		
		
		
		
		
		
		
		// 금월 일자별 영상 열람승인 현황
		tvoViewAprvStateDaily: function (data) {
			let oSeries = [];
			let oCategories = [];

			if (data.length) {
				let oCntViewRqst = {name: '열람승인', data: []},
					oCntExtnRqst = {name: '기간연장승인', data: []};

				data.forEach((item) => {
					oCntViewRqst.data.push(item.cntViewRqst);
					oCntExtnRqst.data.push(item.cntExtnRqst);
					let sDate = moment(item.ymd, 'YYYYMMDD').date();
					if (sDate < 10) sDate = '0' + sDate;
					oCategories.push(sDate.toString());
				});

				oSeries.push(oCntViewRqst);
				oSeries.push(oCntExtnRqst);
			}

			if (chart.apexCharts.tvoViewAprvStateDaily == null) {
				chart.apexCharts.tvoViewAprvStateDaily = new ApexCharts(document.querySelector('#chart-view_aprv-state-daily'), {
					series: oSeries,
					chart: {
						height: 240,
						type: 'line',
						zoom: {
							enabled: false
						},
						toolbar: {
							show: true,
							tools: {
								download: false
							},
						},
					},
					colors: ['#d9534f', '#f0ad4e'],
					stroke: {
						width: 2,
					},
					legend: {
						fontSize: '14px',
						position: 'top',
						horizontalAlign: 'right',
						labels: {
							useSeriesColors: ['#d9534f', '#f0ad4e'],
						},
						onItemClick: {
							toggleDataSeries: false
						},
						onItemHover: {
							highlightDataSeries: false
						},
					},
					markers: {
						size: 4,
						colors: ['#d9534f', '#f0ad4e'],
						strokeWidth: 0,
						shape: "circle",
						radius: 4,
					},
					xaxis: {
						categories: oCategories,
						tickPlacement: 'between',
						labels: {
							show: true,
							style: {
								colors: '#ccc',
								fontSize: '15px',
							},
						},
						tooltip: {
							enabled: false
						},
					},
					yaxis: {
						labels: {
							show: true,
							style: {
								colors: '#ccc',
								fontSize: '15px',
							},
						},
					},
					tooltip: {
						y: [{
							title: {
								formatter: function (val) {
									return val + " :"
								}
							}
						},
							{
								title: {
									formatter: function (val) {
										return val + " :"
									}
								}
							},
							{
								title: {
									formatter: function (val) {
										return val + " :"
									}
								}
							}]
					},
					grid: {
						borderColor: '#666a7e',
					},
				});
				chart.apexCharts.tvoViewAprvStateDaily.render();
			} else {
				chart.apexCharts.tvoViewAprvStateDaily.updateOptions({
					series: oSeries,
					xaxis: {
						categories: oCategories,
						tickPlacement: 'between',
						labels: {
							show: true,
							style: {
								colors: '#ccc',
								fontSize: '15px',
							},
						},
						tooltip: {
							enabled: false
						},
					},
				});
			}
		},

		// 금월 일자별 영상 반출승인 현황
		tvoOutAprvStateDaily: function (data) {
			let oSeries = [];
			let oCategories = [];

			if (data.length) {
				let oCntOutRqst = {name: '반출승인', data: []},
					oCntExtnRqst = {name: '기간연장승인', data: []};

				data.forEach((item) => {
					oCntOutRqst.data.push(item.cntOutRqst);
					oCntExtnRqst.data.push(item.cntExtnRqst);
					let sDate = moment(item.ymd, 'YYYYMMDD').date();
					if (sDate < 10) sDate = '0' + sDate;
					oCategories.push(sDate.toString());
				});

				oSeries.push(oCntOutRqst);
				oSeries.push(oCntExtnRqst);
			}

			if (chart.apexCharts.tvoOutAprvStateDaily == null) {
				chart.apexCharts.tvoOutAprvStateDaily = new ApexCharts(document.querySelector('#chart-out_aprv-state-daily'), {
					series: oSeries,
					chart: {
						height: 240,
						type: 'line',
						zoom: {
							enabled: false
						},
						toolbar: {
							show: true,
							tools: {
								download: false
							},
						},
					},
					colors: ['#5cb85c', '#337ab7'],
					stroke: {
						width: 2,
					},
					legend: {
						fontSize: '14px',
						position: 'top',
						horizontalAlign: 'right',
						labels: {
							useSeriesColors: ['#5cb85c', '#337ab7'],
						},
						onItemClick: {
							toggleDataSeries: false
						},
						onItemHover: {
							highlightDataSeries: false
						},
					},
					markers: {
						size: 4,
						colors: ['#5cb85c', '#337ab7'],
						strokeWidth: 0,
						shape: "circle",
						radius: 4,
					},
					xaxis: {
						categories: oCategories,
						tickPlacement: 'between',
						labels: {
							show: true,
							style: {
								colors: '#ccc',
								fontSize: '15px',
							},
						},
						tooltip: {
							enabled: false
						},
					},
					yaxis: {
						labels: {
							show: true,
							style: {
								colors: '#ccc',
								fontSize: '15px',
							},
						},
					},
					tooltip: {
						y: [{
							title: {
								formatter: function (val) {
									return val + " :"
								}
							}
						},
							{
								title: {
									formatter: function (val) {
										return val + " :"
									}
								}
							},
							{
								title: {
									formatter: function (val) {
										return val + " :"
									}
								}
							}]
					},
					grid: {
						borderColor: '#666a7e',
					},
				});
				chart.apexCharts.tvoOutAprvStateDaily.render();
			} else {
				chart.apexCharts.tvoOutAprvStateDaily.updateOptions({
					series: oSeries,
					xaxis: {
						categories: oCategories,
						tickPlacement: 'between',
						labels: {
							show: true,
							style: {
								colors: '#ccc',
								fontSize: '15px',
							},
						},
						tooltip: {
							enabled: false
						},
					},
				});
			}
		},
		
		
		
		// 금월 영상 열람승인 현황
		tvoViewAprvStateMonthly: function (data) {
			let oSeries = [0, 0];
			if (data != null) {
				oSeries[0] = data.cntViewRqst;
				oSeries[1] = data.cntExtnRqst;
			}

			if (chart.apexCharts.tvoViewAprvStateMonthly == null) {
				chart.apexCharts.tvoViewAprvStateMonthly = new ApexCharts(document.querySelector('#chart-view_aprv-state-monthly'), {
					series: oSeries,
					chart: {
						type: 'donut',
						height: 180,
					},
					colors: ['#d9534f', '#f0ad4e'],
					labels: ['열람승인', '기간연장승인(열람)'],
					legend: {
						fontSize: '16px',
						labels: {
							useSeriesColors: ['#d9534f', '#f0ad4e'],
						},
						onItemClick: {
							toggleDataSeries: false
						},
						onItemHover: {
							highlightDataSeries: false
						},
					},
					stroke: {
						show: false,
					},
					dataLabels: {
						enabled: false,
					},
					plotOptions: {
						pie: {
							donut: {
								size: '84%',
								background: '#191b50',
								labels: {
									show: true,
									total: {
										showAlways: true,
										show: true,
										label: moment().year() + '년 ' + (moment().month() < 9 ? '0' + (moment().month() + 1) : (moment().month() + 1)) + '월',
										fontSize: '16px',
										color: '#fffefe',
									},
									value: {
										show: true,
										fontSize: '36px',
										color: '#fffefe',
									},
								},
							},
						},
					},
				});
				chart.apexCharts.tvoViewAprvStateMonthly.render();
			} else {
				chart.apexCharts.tvoViewAprvStateMonthly.updateOptions({
					series: oSeries
				});
			}
		},

		// 금월 영상 반출승인 현황
		tvoOutAprvStateMonthly: function (data) {
			let oSeries = [0, 0];
			if (data != null) {
				oSeries[0] = data.cntOutRqst;
				oSeries[1] = data.cntExtnRqst;
			}

			if (chart.apexCharts.tvoOutAprvStateMonthly == null) {
				chart.apexCharts.tvoOutAprvStateMonthly = new ApexCharts(document.querySelector('#chart-out_aprv-state-monthly'), {
					series: oSeries,
					chart: {
						type: 'donut',
						height: 180,
					},
					colors: ['#5cb85c', '#337ab7'],
					labels: ['반출승인', '기간연장승인(반출)'],
					legend: {
						fontSize: '16px',
						labels: {
							useSeriesColors: ['#5cb85c', '#337ab7'],
						},
						onItemClick: {
							toggleDataSeries: false
						},
						onItemHover: {
							highlightDataSeries: false
						},
					},
					stroke: {
						show: false,
					},
					dataLabels: {
						enabled: false,
					},
					plotOptions: {
						pie: {
							donut: {
								size: '84%',
								background: '#191b50',
								labels: {
									show: true,
									total: {
										showAlways: true,
										show: true,
										label: moment().year() + '년 ' + (moment().month() < 9 ? '0' + (moment().month() + 1) : (moment().month() + 1)) + '월',
										fontSize: '16px',
										color: '#fffefe',
									},
									value: {
										show: true,
										fontSize: '36px',
										color: '#fffefe',
									},
								},
							},
						},
					},
				});
				chart.apexCharts.tvoOutAprvStateMonthly.render();
			} else {
				chart.apexCharts.tvoOutAprvStateMonthly.updateOptions({
					series: oSeries
				});
			}
		},
		
	}
}
